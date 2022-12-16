package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
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
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.*;

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
    void setup() {
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
        when(orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(order);

        orderService.deleteById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"));

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
        when(orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(order);

        OrderDTO orderReturned = orderService.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
    }

    @ParameterizedTest
    @MethodSource("params")
    void shouldReturnOrdersByParams(int expectedAmountOfOrders, String date, String typeOfOrder, UUID employeeId, String username) {
        // given
        Order order1 = new Order(null, 12.98, "2022-08-24", "12:00:00", null, createOnsiteType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook()), createUser());
        Order order2 = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", createOnsiteType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook(), createWaiter()), createUser());
        Order order3 = new Order(null, 12.98, "2022-08-23", "12:00:00", "12:15:00", createOnsiteType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook(), createWaiter()), createAdmin());
        Order order4 = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", createDeliveryType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook(), createDelivery()), createUser());
        Order order5 = new Order(null, 12.98, "2022-08-23", "12:00:00", "12:15:00", createDeliveryType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook(), createDelivery()), createAdmin());
        List<Order> expectedOrders = List.of(order1, order2, order3, order4, order5);

        // when
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<OrderDTO> ordersReturned = orderService.findByParams(date, typeOfOrder, employeeId, username);

        // then
        assertEquals(expectedAmountOfOrders, ordersReturned.size());
    }

    private static Stream<Arguments> params() {
        return Stream.of(
                Arguments.of(5, null, null, null, null),
                Arguments.of(2, "2022-08-23", null, null, null),
                Arguments.of(3, null, "On-site", null, null),
                Arguments.of(2, null, null, UUID.fromString("04b4f06c-7ad0-11ed-a1eb-0242ac120002"), null),
                Arguments.of(1, "2022-08-23", null, UUID.fromString("04b4f06c-7ad0-11ed-a1eb-0242ac120002"), null),
                Arguments.of(1, "2022-08-23", "On-site", null, null),
                Arguments.of(2, null, "On-site", UUID.fromString("04b4f06c-7ad0-11ed-a1eb-0242ac120002"), null),
                Arguments.of(1, "2022-08-23", "On-site", UUID.fromString("04b4f06c-7ad0-11ed-a1eb-0242ac120002"), null)
        );
    }

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

    @Test
    void shouldReturnAllOrdersByUser() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findByUserUsername("user")).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findAllByUsername("user");

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOrderByIdByUser() throws OrderNotFoundException {
        // given
        List<Order> orders = createOrders();
        Optional<Order> order = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findByUserUsername("user")).thenReturn(orders);
        when(orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(order);

        OrderDTO orderReturned = orderService.findByIdAndUsername(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"), "user").orElse(null);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
    }

    @Test
    void shouldReturnMadeOrdersByUser() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNotNullAndUserUsername("user")).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findMadeOrdersAndUsername("user");

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnUnmadeOrdersByUser() {
        // given
        List<Order> orders = createOrders();

        // when
        when(orderRepository.findOrdersByHourAwayIsNullAndUserUsername("user")).thenReturn(orders);

        List<OrderDTO> ordersReturned = orderService.findUnmadeOrdersAndUsername("user");

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }
}