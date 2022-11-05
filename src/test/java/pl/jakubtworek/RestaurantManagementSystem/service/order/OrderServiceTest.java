package pl.jakubtworek.RestaurantManagementSystem.service.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.OrderServiceImpl;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.TypeOfOrderServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verify;

public class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    private OrderService orderService;

    @BeforeEach
    public void setUp() {
        orderRepository = mock(OrderRepository.class);

        orderService = new OrderServiceImpl(orderRepository);
    }

    @Test
    public void shouldReturnAllOrders() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findAll();

        // then
        assertEquals(3,ordersReturned.size());
    }

    @Test
    public void shouldReturnOneOrder() {
        // given
        Optional<Order> order = Optional.of(new Order());
        when(orderRepository.findById(1L)).thenReturn(order);

        // when
        Optional<Order> orderReturned = orderService.findById(1L);

        // then
        assertNotNull(orderReturned);
    }

    @Test
    public void shouldReturnCreatedOrder(){
        // given
        Order order = spy(new Order());
        when(orderRepository.save(order)).thenReturn(order);

        // when
        Order orderReturned = orderService.save(order);

        // then
        assertNotNull(orderReturned);
    }

    @Test
    public void verifyIsOrderIsDeleted(){
        // when
        orderService.deleteById(1L);

        // then
        verify(orderRepository).deleteById(1L);
    }

    @Test
    public void shouldReturnOrders_whenDateIsPass() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findByDate("Date")).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findByDate("Date");

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
        when(orderRepository.findByEmployees(employee)).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findByEmployee(employee);

        // then
        assertEquals(3,ordersReturned.size());
    }

    @Test
    public void shouldReturnAllMadeOrders() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findMadeOrders();

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    public void shouldReturnAllUnmadeOrders() {
        // given
        List<Order> orders = createOrders();
        when(orderRepository.findAll()).thenReturn(orders);

        // when
        List<Order> ordersReturned = orderService.findUnmadeOrders();

        // then
        assertEquals(1,ordersReturned.size());
    }


    private List<Order> createOrders() {
        List<Order> orders = new ArrayList<>();
        Order order1 = new Order();
        order1.setHourAway("12:00");
        Order order2 = new Order();
        order2.setHourAway("12:00");
        Order order3 = new Order();
        orders.add(order1);
        orders.add(order2);
        orders.add(order3);
        return orders;
    }
}
