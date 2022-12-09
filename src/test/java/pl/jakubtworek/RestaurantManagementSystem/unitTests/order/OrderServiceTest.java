package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.OrderServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private TypeOfOrderRepository typeOfOrderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private OrderFactory orderFactory;
    @Mock
    private OrdersQueueFacade ordersQueueFacade;

    private OrderService orderService;

    @BeforeEach
    void setup(){
        orderRepository = mock(OrderRepository.class);
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);
        userRepository = mock(UserRepository.class);
        menuItemRepository = mock(MenuItemRepository.class);
        orderFactory = mock(OrderFactory.class);
        ordersQueueFacade = mock(OrdersQueueFacade.class);

        orderService = new OrderServiceImpl(
                orderRepository,
                typeOfOrderRepository,
                userRepository,
                menuItemRepository,
                orderFactory,
                ordersQueueFacade
        );
    }

    @Test
    void shouldReturnAllOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    void shouldReturnOneOrder() {
        // given
        Optional<Order> order = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(1L)).thenReturn(order);

        Optional<OrderDTO> orderReturned = orderService.findById(1L);

        // then
        assertNotNull(orderReturned);
    }

    @Test
    void shouldReturnCreatedOrder() throws Exception {
        // given
        Order order = createOnsiteOrder();
        OrderRequest orderRequest = createOnsiteOrderRequest();

        // when
        when(orderFactory.createOrder(any(), any(), anyList(), any())).thenReturn(order.convertEntityToDTO());
        when(orderRepository.save(order)).thenReturn(order);
        doNothing().when(ordersQueueFacade).addToQueue(order.convertEntityToDTO());

        OrderDTO orderReturned = orderService.save(orderRequest);

        // then
        assertNotNull(orderReturned);
    }


    @Test
    void verifyIsOrderIsDeleted() throws OrderNotFoundException {
        // when
        orderService.deleteById(1L);

        // then
        verify(orderRepository).deleteById(1L);
    }

/*    @Test
    void shouldReturnOrders_whenDateIsPass() {
        // given
        Optional<List<Order>> orders = Optional.of(createOrders());

        // when
        when(orderRepository.findByDate("Date")).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findByDate("Date");

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenTypeOfOrderIsPass() {
        // given
        Optional<List<Order>> orders = Optional.of(createOrders());
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        when(orderRepository.findByTypeOfOrder(typeOfOrder)).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findByTypeOfOrder(typeOfOrder);

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenEmployeeIsPass() {
        // given
        Optional<List<Order>> orders = Optional.of(createOrders());

        // when
        when(orderRepository.findByEmployeesId(1L)).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findByEmployeeId(1L);

        // then
        assertEquals(2,ordersReturned.size());
    }*/


    @Test
    void shouldReturnAllMadeOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findMadeOrders();

        // then
        assertEquals(2,ordersReturned.size());
    }

    @Test
    void shouldReturnAllUnmadeOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findUnmadeOrders();

        // then
        assertEquals(2,ordersReturned.size());
    }
}
