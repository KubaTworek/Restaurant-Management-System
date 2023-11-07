package pl.jakubtworek.order;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.auth.dto.SimpleUserSnapshot;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFacadeTest {
    @Mock
    private UserFacade userFacade;
    @Mock
    private EmployeeFacade employeeFacade;
    @Mock
    private MenuItemFacade menuItemFacade;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderQueryRepository orderQueryRepository;
    @Mock
    private DomainEventPublisher publisher;

    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderFacade = new OrderFacade(userFacade, employeeFacade, menuItemFacade, orderRepository, orderQueryRepository, publisher);
    }

    @Test
    void shouldSetOrderAsDelivered() {
        // given
        final var orderId = 1L;
        final var expectedOrder = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);

        // when
        orderFacade.setAsDelivered(orderId);

        // then
        verify(orderRepository).save(expectedOrder);
    }

    @Test
    void shouldThrowException_whenOrderToDeliveredIsNotFound() {
        // given
        final var orderId = 1L;
        final var order = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(IllegalStateException.class, () -> orderFacade.setAsDelivered(orderId));
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // given
        final var orderId = 1L;
        final var employeeId = 1L;
        final var employee = new SimpleEmployee(employeeId, "John", "Doe", Job.COOK);
        final var expectedOrder = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(expectedOrder));
        when(orderRepository.save(expectedOrder)).thenReturn(expectedOrder);
        when(employeeFacade.getById(employeeId)).thenReturn(employee);

        // when
        orderFacade.addEmployeeToOrder(orderId, employeeId);

        // then
        verify(orderRepository).save(expectedOrder);
    }

    @Test
    void shouldThrowException_whenOrderToAddEmployeeIsNotFound() {
        // given
        final var orderId = 1L;
        final var employeeId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(IllegalStateException.class, () -> orderFacade.addEmployeeToOrder(orderId, employeeId));
    }

    @Test
    void shouldReturnNumberOfMenuItems() {
        // given
        final var orderId = 1L;
        final var simpleOrder = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var order = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var pizza = new SimpleMenuItem(1L, "Pizza", 120);
        final var spaghetti = new SimpleMenuItem(2L, "Spaghetti", 100);
        order.addMenuItem(pizza);
        order.addMenuItem(spaghetti);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        final int result = orderFacade.getNumberOfMenuItems(simpleOrder);

        // then
        assertEquals(2, result);
    }

    @Test
    void shouldThrowException_whenOrderToGetMenuItemsIsNotFound() {
        // given
        final var orderId = 1L;
        final var order = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalStateException.class, () -> orderFacade.getNumberOfMenuItems(order));
    }

    @Test
    void shouldSaveOrderAndAddToQueue() {
        // given
        final var request = new OrderRequest("ON_SITE", List.of("Pizza", "Spaghetti"));
        final var expectedUser = new SimpleUser(1L, "john.doe");
        final var expectedSimpleOrder = new SimpleOrder(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var expectedOrder = createOrder(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var pizza = new SimpleMenuItem(1L, "Pizza", 120);
        final var spaghetti = new SimpleMenuItem(2L, "Spaghetti", 100);

        when(userFacade.getByToken("jwt-token")).thenReturn(expectedUser);
        when(menuItemFacade.getByName("Pizza")).thenReturn(pizza);
        when(menuItemFacade.getByName("Spaghetti")).thenReturn(spaghetti);
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        // when
        final OrderDto result = orderFacade.save(request, "jwt-token");

        // then
        assertEquals(expectedOrder.getSnapshot().getId(), result.getId());
        assertEquals(expectedOrder.getSnapshot().getPrice(), result.getPrice());
    }

    @Test
    void shouldFindAllOrdersForUser() {
        // given
        final var expectedUser = new SimpleUser(1L, "john.doe");
        final var expectedOrders = createOrderDtos();

        when(userFacade.getByToken("jwt-token")).thenReturn(expectedUser);
        when(orderQueryRepository.findByUserUsername("john.doe")).thenReturn(expectedOrders);

        // when
        final List<OrderDto> result = orderFacade.findAll("jwt-token");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindOrderById() {
        // given
        final var orderId = 1L;
        final var expectedOrder = OrderDto.create(orderId, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE, null, null);

        when(orderQueryRepository.findDtoById(orderId)).thenReturn(Optional.of(expectedOrder));

        // when
        final Optional<OrderDto> result = orderFacade.findById(orderId);

        // then
        assertEquals(expectedOrder.getId(), result.get().getId());
        assertEquals(expectedOrder.getTypeOfOrder(), result.get().getTypeOfOrder());
        assertEquals(expectedOrder.getPrice(), result.get().getPrice());
        assertEquals(expectedOrder.getHourOrder(), result.get().getHourOrder());
        assertEquals(expectedOrder.getHourAway(), result.get().getHourAway());
    }

    @Test
    void shouldFindOrdersByParams() {
        // given
        final var expectedOrders = createOrderDtos();

        when(orderQueryRepository.findFilteredOrders(any(), any(), any(), any(), any(), any())).thenReturn(expectedOrders);

        // when
        final List<OrderDto> result = orderFacade.findByParams(ZonedDateTime.now().toString(), ZonedDateTime.now().toString(), "ON_SITE", true, 1L, "john.doe");

        // then
        assertEquals(2, result.size());
    }

    private Order createOrder(Long id, Integer price, ZonedDateTime hourOrder, ZonedDateTime hourAway, TypeOfOrder typeOfOrder) {
        return Order.restore(new OrderSnapshot(
                id, price, hourOrder, hourAway, typeOfOrder,
                new HashSet<>(), new HashSet<>(), new SimpleUserSnapshot()
        ));
    }

    private List<OrderDto> createOrderDtos() {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(OrderDto.create(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE, null, null));
        orderDtos.add(OrderDto.create(2L, 250, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY, null, null));
        return orderDtos;
    }

    private Matcher<SimpleOrder> simpleOrderMatcher(SimpleOrder expected) {
        return new TypeSafeMatcher<>() {
            @Override
            protected boolean matchesSafely(SimpleOrder actual) {
                boolean idMatches = Objects.equals(expected.getId(), actual.getId());
                boolean priceMatches = Objects.equals(expected.getPrice(), actual.getPrice());
                boolean hourOrderMatches = Objects.equals(expected.getHourOrder(), actual.getHourOrder());
                boolean hourAwayMatches = Objects.equals(expected.getHourAway(), actual.getHourAway());
                boolean typeOfOrderMatches = expected.getTypeOfOrder() == actual.getTypeOfOrder();

                return idMatches && priceMatches && hourOrderMatches && hourAwayMatches && typeOfOrderMatches;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("expected SimpleOrder fields should match");
            }
        };
    }
}
