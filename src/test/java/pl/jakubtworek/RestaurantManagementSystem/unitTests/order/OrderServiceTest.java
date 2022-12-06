/*
package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.OrderServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderFactory orderFactory;
    @Mock
    private OrdersQueueFacade ordersQueueFacade;

    private OrderService orderService;

    @BeforeEach
    public void setup(){
        orderRepository = mock(OrderRepository.class);
        orderFactory = mock(OrderFactory.class);
        ordersQueueFacade = mock(OrdersQueueFacade.class);

        orderService = new OrderServiceImpl(
                orderRepository,
                orderFactory,
                ordersQueueFacade
        );
    }

    @Test
    public void shouldReturnAllOrders() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    public void shouldReturnOneOrder() {
        // given
        Optional<Order> order = Optional.of(new Order());
        when(orderRepository.findById(1L)).thenReturn(order);

        // when
        Optional<OrderDTO> orderReturned = orderService.findById(1L);

        // then
        assertNotNull(orderReturned);
    }

*/
/*    @Test
    public void shouldReturnCreatedOrder(){
        // given
        Order order = spy(new Order());
        OrderDTO orderDTO = spy(new OrderDTO());
        when(orderRepository.save(order)).thenReturn(order);

        // when
        Order orderReturned = orderService.save(orderDTO);

        // then
        assertNotNull(orderReturned);
    }*//*


    @Test
    public void verifyIsOrderIsDeleted(){
        // when
        orderService.deleteById(1L);

        // then
        verify(orderRepository).deleteById(1L);
    }

*/
/*    @Test
    public void shouldReturnOrders_whenDateIsPass() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findByDate("Date")).thenReturn(orders);

        // when
        List<OrderDTO> ordersReturned = orderService.findByDate("Date");

        // then
        assertEquals(3,ordersReturned.size());
    }

    @Test
    public void shouldReturnOrders_whenTypeOfOrderIsPass() {
        // given
        List<Order> orders = createOrders();
        TypeOfOrder typeOfOrder = spy(new TypeOfOrder());
        when(orderRepository.findByTypeOfOrder(typeOfOrder)).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findByTypeOfOrder(typeOfOrder);

        // then
        assertEquals(3,ordersReturned.size());
    }

    @Test
    public void shouldReturnOrders_whenEmployeeIsPass() {
        // given
        List<Order> orders = createOrders();
        Employee employee = spy(new Employee());
        when(orderRepository.findByEmployee(employee)).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findByEmployee(employee);

        // then
        assertEquals(3,ordersReturned.size());
    }*//*


*/
/*    @Test
    public void shouldReturnAllMadeOrders() {
        // given
        Optional<Order> orders = Optional.of(createOnsiteOrder().get());
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(orders);

        // when
        List<OrderDTO> ordersReturned = orderService.findMadeOrders();

        // then
        assertEquals(1,ordersReturned.size());
    }

    @Test
    public void shouldReturnAllUnmadeOrders() {
        // given
        Optional<Order> orders = Optional.of(createDeliveryOrder().get());
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(orders);

        // when
        List<OrderDTO> ordersReturned = orderService.findUnmadeOrders();

        // then
        assertEquals(1,ordersReturned.size());
    }*//*

}

*/
