package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.*;

@SpringBootTest
class OrderControllerIT {
    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private TypeOfOrderRepository typeOfOrderRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private MenuItemRepository menuItemRepository;
    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;

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
        when(orderRepository.save(any())).thenReturn(order);
        when(userRepository.findByUsername(any())).thenReturn(Optional.of(user));
        when(typeOfOrderRepository.findByType(any())).thenReturn(Optional.of(typeOfOrder));
        when(menuItemRepository.findByName(any())).thenReturn(Optional.of(menuItem));

        OrderResponse orderCreated = orderController.saveOrder(orderRequest).getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrder(orderCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<Order> expectedOrder = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(expectedOrder);

        String response = orderController.deleteOrder(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Order with id: 8e4087ce-7846-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnAllOrders() {
        // given
        List<Order> expectedOrders = createOrders();

        // when
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrders().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // given
        Optional<Order> expectedOrder = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(expectedOrder);

        OrderResponse orderReturned = orderController.getOrderById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrder(orderReturned);
    }

    @Test
    void shouldThrowException_whenOrderNotExist() {
        // when
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(UUID.fromString("a0f7ae28-7847-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no order in restaurant with that id: a0f7ae28-7847-11ed-a1eb-0242ac120002", exception.getMessage());
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

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(date, typeOfOrder, employeeId, username).getBody();

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
        List<Order> expectedOrders = List.of(createOnsiteOrder());

        // when
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderMade().getBody();

        // then
        assertEquals(12.98, ordersReturned.get(0).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(0).getDate());
        assertEquals("12:00:00", ordersReturned.get(0).getHourOrder());
        assertEquals("12:15:00", ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(0).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(0).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(0).getEmployees().get(0).getJob().getName());
    }


    @Test
    void shouldReturnUnmadeOrders() {
        // given
        List<Order> expectedOrders = List.of(createDeliveryOrder());

        // when
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderUnmade().getBody();

        // then
        assertEquals(7.98, ordersReturned.get(0).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(0).getDate());
        assertEquals("12:05:00", ordersReturned.get(0).getHourOrder());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("Delivery", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(0).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(0).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(0).getEmployees().get(0).getJob().getName());
    }
}