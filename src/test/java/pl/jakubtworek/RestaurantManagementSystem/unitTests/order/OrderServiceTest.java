package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.OrderServiceImpl;

import java.util.*;

import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createChickenMenuItem;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.createUser;

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
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private OrderService orderService;

    @BeforeEach
    void setup(){
        orderRepository = mock(OrderRepository.class);
        typeOfOrderRepository = mock(TypeOfOrderRepository.class);
        userRepository = mock(UserRepository.class);
        menuItemRepository = mock(MenuItemRepository.class);
        orderFactory = mock(OrderFactory.class);
        ordersQueueFacade = mock(OrdersQueueFacade.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);

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
    void shouldReturnCreatedOrder() throws Exception {
        // given
        Order order = createOnsiteOrder();
        User user = createUser();
        TypeOfOrder typeOfOrder = createOnsiteType();
        MenuItem menuItem = createChickenMenuItem();
        OrderRequest orderRequest = createOnsiteOrderRequest();

        // when
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");
        when(orderFactory.createOrder(any(), any(), anyList(), any())).thenReturn(order.convertEntityToDTO());
        when(orderRepository.save(any())).thenReturn(order);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(typeOfOrderRepository.findByType(any())).thenReturn(Optional.of(typeOfOrder));
        when(menuItemRepository.findByName(any())).thenReturn(Optional.of(menuItem));
        doNothing().when(ordersQueueFacade).addToQueue(order.convertEntityToDTO());

        OrderDTO orderCreated = orderService.save(orderRequest);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderCreated);
    }

    @Test
    void verifyIsOrderIsDeleted() throws OrderNotFoundException {
        // given
        Optional<Order> order = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(1L)).thenReturn(order);

        orderService.deleteById(1L);

        // then
        verify(orderRepository).delete(order.get());
    }

    @Test
    void shouldReturnAllOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findAll()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOrderById() {
        // given
        Optional<Order> order = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(1L)).thenReturn(order);

        OrderDTO orderReturned = orderService.findById(1L).orElse(null);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
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
    void shouldReturnMadeOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findMadeOrders();

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnUnmadeOrders() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findUnmadeOrders();

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }
}
