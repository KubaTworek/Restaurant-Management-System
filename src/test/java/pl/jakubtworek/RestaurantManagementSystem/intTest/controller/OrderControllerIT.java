package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createCook;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
class OrderControllerIT {

    @Autowired
    private OrderController orderController;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private TypeOfOrderRepository typeOfOrderRepository;
    @MockBean
    private EmployeeRepository employeeRepository;

    @Test
    void shouldReturnAllOrders() {
        // given
        List<Order> expectedOrders = createOrders();

        // when
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrders().getBody();

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

        assertEquals(30.99, ordersReturned.get(1).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(1).getDate());
        assertEquals("12:05", ordersReturned.get(1).getHourOrder());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(1).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(1).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(1).getEmployees().get(0).getJob().getName());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // given
        Optional<Order> expectedOrder = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        OrderResponse orderReturned = orderController.getOrderById(1L).getBody();

        // then
        assertEquals(12.99, orderReturned.getPrice());
        assertEquals("2022-08-22", orderReturned.getDate());
        assertEquals("12:00", orderReturned.getHourOrder());
        assertEquals("12:15", orderReturned.getHourAway());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals("Chicken", orderReturned.getMenuItems().get(0).getName());
        assertEquals(10.99, orderReturned.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderReturned.getMenuItems().get(1).getName());
        assertEquals(1.99, orderReturned.getMenuItems().get(1).getPrice());
        assertEquals("John", orderReturned.getEmployees().get(0).getFirstName());
        assertEquals("Smith", orderReturned.getEmployees().get(0).getLastName());
        assertEquals("Cook", orderReturned.getEmployees().get(0).getJob().getName());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingOrder() {
        // when
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(3L));

        // then
        assertEquals("There are no order in restaurant with that id: 3", exception.getMessage());
    }

/*    @Test
    void shouldReturnCreatedOrder() throws Exception {

    }*/


    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<Order> expectedOrder = Optional.of(createOnsiteOrder());

        // when
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        OrderResponse orderDeleted = orderController.deleteOrder(1L).getBody();

        // then
        assertEquals(12.99, orderDeleted.getPrice());
        assertEquals("2022-08-22", orderDeleted.getDate());
        assertEquals("12:00", orderDeleted.getHourOrder());
        assertEquals("12:15", orderDeleted.getHourAway());
        assertEquals("On-site", orderDeleted.getTypeOfOrder().getType());
        assertEquals("Chicken", orderDeleted.getMenuItems().get(0).getName());
        assertEquals(10.99, orderDeleted.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderDeleted.getMenuItems().get(1).getName());
        assertEquals(1.99, orderDeleted.getMenuItems().get(1).getPrice());
        assertEquals("John", orderDeleted.getEmployees().get(0).getFirstName());
        assertEquals("Smith", orderDeleted.getEmployees().get(0).getLastName());
        assertEquals("Cook", orderDeleted.getEmployees().get(0).getJob().getName());
    }

    @Test
    void shouldReturnOrders_whenDateIsPassed() {
        // given
        Optional<List<Order>> expectedOrders = Optional.of(createOrders());

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
        Optional<List<Order>> expectedOrders = Optional.of(List.of(createOnsiteOrder()));

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
        Optional<List<Order>> expectedOrders = Optional.of(createOrders());
        Optional<Employee> employee = Optional.of(createCook());

        // when
        when(employeeRepository.findById(1L)).thenReturn(employee);
        when(orderRepository.findByEmployeesId(1L)).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(null, null, 1L).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }


    @Test
    void shouldReturnMadeOrders() {
        // given
        Optional<List<Order>> expectedOrders = Optional.of(List.of(createOnsiteOrder()));

        // when
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderMade().getBody();

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
    void shouldReturnUnmadeOrders() {
        // given
        Optional<List<Order>> expectedOrders = Optional.of(List.of(createDeliveryOrder()));

        // when
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderUnmade().getBody();

        // then
        assertEquals(30.99, ordersReturned.get(0).getPrice());
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
