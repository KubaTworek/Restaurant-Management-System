package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
    private Kitchen kitchen;

    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderFacade = new OrderFacade(userFacade, employeeFacade, menuItemFacade, orderRepository, orderQueryRepository, kitchen);
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

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(IllegalStateException.class, () -> orderFacade.setAsDelivered(orderId));
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // given
        final var orderId = 1L;
        final var employeeId = 1L;
        final var employee = EmployeeDto.create(employeeId, "John", "Doe", Job.COOK);
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
    void shouldSaveOrderAndAddToQueue() {
        // given
        final var request = new OrderRequest("ON_SITE", List.of("Pizza", "Spaghetti"));
        final var expectedUser = UserDto.create(1L, "john.doe", "password");
        final var expectedSimpleOrder = OrderDto.create(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var expectedOrder = createOrder(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var pizza = MenuItemDto.create(1L, "Pizza", 120);
        final var spaghetti = MenuItemDto.create(2L, "Spaghetti", 100);

        when(userFacade.getByToken("jwt-token")).thenReturn(expectedUser);
        when(menuItemFacade.getByName("Pizza")).thenReturn(pizza);
        when(menuItemFacade.getByName("Spaghetti")).thenReturn(spaghetti);
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        // when
        final var result = orderFacade.save(request, "jwt-token");

        // then
        assertEquals(expectedOrder.getSnapshot().getId(), result.getId());
        assertEquals(expectedOrder.getSnapshot().getPrice(), result.getPrice());
    }

    @Test
    void shouldFindAllOrdersForUser() {
        // given
        final var expectedUser = UserDto.create(1L, "john.doe", "password");
        final var expectedOrders = createOrderDtos();

        when(userFacade.getByToken("jwt-token")).thenReturn(expectedUser);
        when(orderQueryRepository.findByUserId(1L)).thenReturn(expectedOrders);

        // when
        final List<OrderDto> result = orderFacade.findAll("jwt-token");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindOrdersByParams() {
        // given
        final var expectedOrders = createOrderDtos();

        when(orderQueryRepository.findFilteredOrders(any(), any(), any(), any(), any(), any())).thenReturn(expectedOrders);

        // when
        final List<OrderDto> result = orderFacade.findByParams(ZonedDateTime.now().toString(), ZonedDateTime.now().toString(), "ON_SITE", true, 1L, 1L);

        // then
        assertEquals(2, result.size());
    }

    private Order createOrder(Long id, Integer price, ZonedDateTime hourOrder, ZonedDateTime hourAway, TypeOfOrder typeOfOrder) {
        return Order.restore(new OrderSnapshot(
                id, price, hourOrder, hourAway, typeOfOrder,
                new HashSet<>(), new HashSet<>(), new UserId(1L)
        ));
    }

    private List<OrderDto> createOrderDtos() {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(OrderDto.create(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE));
        orderDtos.add(OrderDto.create(2L, 250, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY));
        return orderDtos;
    }
}
