package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
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

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createChickenMenuItem;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.createUser;

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
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        String response = orderController.deleteOrder(1L).getBody();

        // then
        assertEquals("Order with id: 1 was deleted", response);
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
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        OrderResponse orderReturned = orderController.getOrderById(1L).getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrder(orderReturned);
    }

    @Test
    void shouldThrowException_whenOrderNotExist() {
        // when
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(3L));

        // then
        assertEquals("There are no order in restaurant with that id: 3", exception.getMessage());
    }



/*    @Test
    void shouldReturnOrdersByDate() {
        // given
        List<Order> expectedOrders = createOrders();

        // when
        when(orderRepository.findByDate("2022-08-22")).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams("2022-08-22", null, null).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenTypeOfOrderIsPassed() {
        // given
        TypeOfOrder expectedTypeOfOrder = createOnsiteType();
        List<Order> expectedOrders = List.of(createOnsiteOrder());

        // when
        when(typeOfOrderRepository.findByType(any())).thenReturn(Optional.of(expectedTypeOfOrder));
        when(orderRepository.findByTypeOfOrder(any())).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(null, "On-site", null).getBody();

        // then
        assertEquals(12.99, ordersReturned.get(0).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(0).getDate());
        assertEquals("12:00", ordersReturned.get(0).getHourOrder());
        assertEquals("12:15", ordersReturned.get(0).getHourAway());
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
    void shouldReturnOrders_whenEmployeeIsPassed() {
        // given
        List<Order> expectedOrders = createOrders();
        Optional<Employee> employee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(1L)).thenReturn(employee);
        when(orderRepository.findByEmployeesId(1L)).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(null, null, 1L).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }*/


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
        assertEquals("12:00", ordersReturned.get(0).getHourOrder());
        assertEquals("12:15", ordersReturned.get(0).getHourAway());
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
        assertEquals("12:05", ordersReturned.get(0).getHourOrder());
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
