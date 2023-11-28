package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderTest {
    @Mock
    private OrderRepository repository;
    @Mock
    private DomainEventPublisher publisher;
    @Captor
    private ArgumentCaptor<OrderEvent> event;

    private Order order;
    private List<ItemDto> items;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        order = new Order();
        order.setDependencies(
                publisher,
                repository
        );
        items = createItems();

        when(repository.save(order)).thenReturn(order);
        when(repository.findById(1L)).thenReturn(Optional.of(order));
    }

    @Test
    void shouldRestoreOrderFromSnapshot_whenDepthIsZero() {
        // given
        final var orderSnapshot = new OrderSnapshot(1L, BigDecimal.valueOf(50), ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                TypeOfOrder.ON_SITE, Collections.emptySet(), new HashSet<>(), new CustomerId(1L));

        // when
        final var order = Order.restore(orderSnapshot, 0);

        // then
        assertOrderSnapshotsEqual(orderSnapshot, order.getSnapshot(1));
    }

    @Test
    void shouldRestoreOrderFromSnapshot_whenDepthIsGreaterThanZero() {
        // given
        final var orderItemSnapshot = new OrderItemSnapshot(1L, "Burger", BigDecimal.valueOf(10), 2, null);
        final var orderSnapshot = new OrderSnapshot(1L, BigDecimal.valueOf(50), ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                TypeOfOrder.ON_SITE, Collections.singleton(orderItemSnapshot), new HashSet<>(), new CustomerId(1L));

        // when
        final var order = Order.restore(orderSnapshot, 1);

        // then
        assertOrderSnapshotsEqual(orderSnapshot, order.getSnapshot(1));
    }

    @ParameterizedTest
    @CsvSource({"ON_SITE", "TAKE_AWAY", "DELIVERY"})
    void shouldCreateOrder(String typeOfOrder) {
        // given
        final var userId = new CustomerId(1L);

        // when
        order.from(items, typeOfOrder, userId);

        // then
        final var result = order.getSnapshot(1);
        assertEquals(BigDecimal.valueOf(34.97), result.getPrice());
        assertEquals(typeOfOrder, result.getTypeOfOrder().toString());
        assertEquals(userId, result.getClientId());
        assertNotNull(result.getHourOrder());
        assertNull(result.getHourAway());
        verify(publisher).publish(event.capture());
        final var eventCaptured = event.getValue();
        assertEquals(result.getId(), eventCaptured.getOrderId());
        assertNull(eventCaptured.getEmployeeId());
        assertEquals(result.getTypeOfOrder(), eventCaptured.getOrderType());
        assertEquals(result.getOrderItems().size(), eventCaptured.getAmountOfMenuItems());
        assertEquals(OrderEvent.State.TODO, eventCaptured.getState());
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // when
        order.addEmployee(1L, new EmployeeId(1L));

        // then
        assertEquals(1, order.getSnapshot(1).getEmployees().stream().findFirst().get().getId());
    }

    @Test
    void shouldSetDeliveryForOrder() {
        // when
        order.setAsDelivered(1L);

        // then
        assertEquals(ZonedDateTime.now().getSecond(), order.getSnapshot(1).getHourAway().getSecond());
    }

    @Test
    void shouldThrowException_whenCreateOrderInfoWithInvalidTypeOfOrder() {
        // when & then
        assertThrows(IllegalStateException.class, () ->
                order.from(createItems(), "INVALID_TYPE_OF_ORDER_TYPE", new CustomerId(1L))
        );
    }

    private void assertOrderSnapshotsEqual(OrderSnapshot expected, OrderSnapshot actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getHourOrder(), actual.getHourOrder());
        assertEquals(expected.getHourAway(), actual.getHourAway());
        assertEquals(expected.getTypeOfOrder(), actual.getTypeOfOrder());
        assertEquals(expected.getOrderItems(), actual.getOrderItems());
        assertEquals(expected.getEmployees(), actual.getEmployees());
        assertEquals(expected.getClientId(), actual.getClientId());
    }

    private List<ItemDto> createItems() {
        final List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        items.add(new ItemDto("Pizza", BigDecimal.valueOf(12.99)));
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        return items;
    }
}
