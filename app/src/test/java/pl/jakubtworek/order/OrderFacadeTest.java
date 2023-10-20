package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.queue.OrdersQueueFacade;

import java.time.ZonedDateTime;
import java.util.ArrayList;
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
    private OrdersQueueFacade ordersQueueFacade;

    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderFacade = new OrderFacade(userFacade, employeeFacade, menuItemFacade, orderRepository, orderQueryRepository, ordersQueueFacade);
    }

    @Test
    void testSetAsDelivered() {
        // given
        final var orderId = 1L;
        final var simpleOrder = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var order = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);

        // when
        orderFacade.setAsDelivered(simpleOrder);

        // then
        verify(orderRepository).save(order);
    }

    @Test
    void testSetAsDeliveredOrderNotFound() {
        // given
        final var orderId = 1L;
        final var order = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(IllegalStateException.class, () -> orderFacade.setAsDelivered(order));
    }

    @Test
    void testAddEmployeeToOrder() {
        // given
        final var orderId = 1L;
        final var employeeId = 1L;
        final var simpleOrder = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var order = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var employee = new SimpleEmployee(employeeId, "John", "Doe", Job.COOK);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(orderRepository.save(order)).thenReturn(order);
        when(employeeFacade.getById(employeeId)).thenReturn(employee);

        // when
        orderFacade.addEmployeeToOrder(simpleOrder, employee);

        // then
        verify(orderRepository).save(order);
    }

    @Test
    void testAddEmployeeToOrderOrderNotFound() {
        // given
        final var orderId = 1L;
        final var employeeId = 1L;
        final var order = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var employee = new SimpleEmployee(employeeId, "John", "Doe", Job.COOK);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when and then
        assertThrows(IllegalStateException.class, () -> orderFacade.addEmployeeToOrder(order, employee));
    }

    @Test
    void testGetNumberOfMenuItems() {
        // given
        final var orderId = 1L;
        final var simpleOrder = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var order = createOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        order.getMenuItems().add(new SimpleMenuItem(1L, "Pizza", 120));
        order.getMenuItems().add(new SimpleMenuItem(2L, "Spaghetti", 100));
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // when
        final int result = orderFacade.getNumberOfMenuItems(simpleOrder);

        // then
        assertEquals(2, result);
    }

    @Test
    void testGetNumberOfMenuItemsOrderNotFound() {
        // given
        final var orderId = 1L;
        final var order = new SimpleOrder(orderId, 200, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // when & then
        assertThrows(IllegalStateException.class, () -> orderFacade.getNumberOfMenuItems(order));
    }

    @Test
    void testSave() {
        // given
        final var orderRequest = new OrderRequest("ON_SITE", List.of("Pizza", "Spaghetti"));
        when(userFacade.getUser("jwt-token")).thenReturn(new SimpleUser(1L, "john.doe"));
        when(menuItemFacade.getByName("Pizza")).thenReturn(new SimpleMenuItem(1L, "Pizza", 120));
        when(menuItemFacade.getByName("Spaghetti")).thenReturn(new SimpleMenuItem(2L, "Spaghetti", 100));
        when(orderRepository.save(any())).thenReturn(createOrder(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE));

        // when
        final OrderDto result = orderFacade.save(orderRequest, "jwt-token");

        // then
        assertEquals(1L, result.getId());
        assertEquals(220, result.getPrice());
    }

    @Test
    void testFindAll() {
        // given
        when(userFacade.getUser("jwt-token")).thenReturn(new SimpleUser(1L, "john.doe"));
        when(orderQueryRepository.findByUserUsername("john.doe")).thenReturn(createOrderDtos());

        // when
        final List<OrderDto> result = orderFacade.findAll("jwt-token");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void testFindById() {
        // given
        final var orderId = 1L;
        final var orderDto = OrderDto.create(orderId, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        when(orderQueryRepository.findDtoById(orderId)).thenReturn(Optional.of(orderDto));

        // when
        final Optional<OrderDto> result = orderFacade.findById(orderId);

        // then
        assertEquals(orderDto.getId(), result.get().getId());
        assertEquals(orderDto.getTypeOfOrder(), result.get().getTypeOfOrder());
        assertEquals(orderDto.getPrice(), result.get().getPrice());
        assertEquals(orderDto.getHourOrder(), result.get().getHourOrder());
        assertEquals(orderDto.getHourAway(), result.get().getHourAway());
    }

    @Test
    void testFindByParams() {
        // given
        when(orderQueryRepository.findFilteredOrders(any(), any(), any(), any(), any(), any())).thenReturn(createOrderDtos());

        // when
        final List<OrderDto> result = orderFacade.findByParams(ZonedDateTime.now(), ZonedDateTime.now(), "ON_SITE", true, 1L, "john.doe");

        // then
        assertEquals(2, result.size());
    }

    private Order createOrder(Long id, Integer price, ZonedDateTime hourOrder, ZonedDateTime hourAway, TypeOfOrder typeOfOrder) {
        final var order = new Order();
        order.setId(id);
        order.setPrice(price);
        order.setHourOrder(hourOrder);
        order.setHourAway(hourAway);
        order.setTypeOfOrder(typeOfOrder);
        return order;
    }

    private List<OrderDto> createOrderDtos() {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(OrderDto.create(1L, 220, ZonedDateTime.now(), null, TypeOfOrder.ON_SITE));
        orderDtos.add(OrderDto.create(2L, 250, ZonedDateTime.now(), null, TypeOfOrder.DELIVERY));
        return orderDtos;
    }
}
