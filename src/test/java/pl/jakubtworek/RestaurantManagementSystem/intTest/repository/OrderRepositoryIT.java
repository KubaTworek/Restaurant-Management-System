package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
@Transactional
class OrderRepositoryIT {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnCreatedOrder() {
        // given
        Order order = createOnsiteOrder();

        // when
        Order orderCreated = orderRepository.save(order);

        // then
        checkAssertionsForOrder(orderCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        orderRepository.deleteById(1L);
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(1, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllOrders() {
        // when
        List<Order> ordersReturned = orderRepository.findAll();

        // then
        checkAssertionsForOrders(ordersReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneOrder() {
        // when
        Order orderReturned = orderRepository.findById(1L).orElse(null);

        // then
        checkAssertionsForOrder(orderReturned);
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassDate() {
        // given
        String date = "2022-08-22";

        // when
        List<Order> orders = orderRepository.findByDate(date);

        // then
        checkAssertionsForOrders(ordersR);
    }


    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassTypeOfOrder() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        List<Order> ordersReturned = orderRepository.findByTypeOfOrder(typeOfOrder);

        // then
        checkAssertionsForOrders(ordersReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassEmployee() {
        // when
        List<Order> ordersReturned = orderRepository.findByEmployeesId(1L);

        // then
        checkAssertionsForOrders(ordersReturned);
    }*/

    private void checkAssertionsForOrder(Order order){
        assertEquals(12.99, order.getPrice());
        assertEquals("2022-08-22", order.getDate());
        assertEquals("12:00", order.getHourOrder());
        assertEquals("12:15", order.getHourAway());
        assertEquals("On-site", order.getTypeOfOrder().getType());
        assertEquals("Chicken", order.getMenuItems().get(0).getName());
        assertEquals(10.99, order.getMenuItems().get(0).getPrice());
        assertEquals("Coke", order.getMenuItems().get(1).getName());
        assertEquals(1.99, order.getMenuItems().get(1).getPrice());
        assertEquals("John", order.getEmployees().get(0).getFirstName());
        assertEquals("Smith", order.getEmployees().get(0).getLastName());
        assertEquals("Cook", order.getEmployees().get(0).getJob().getName());
    }

    private void checkAssertionsForOrders(List<Order> orders){
        assertEquals(12.99, orders.get(0).getPrice());
        assertEquals("2022-08-22", orders.get(0).getDate());
        assertEquals("12:00", orders.get(0).getHourOrder());
        assertEquals("12:15", orders.get(0).getHourAway());
        assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", orders.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, orders.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", orders.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, orders.get(0).getMenuItems().get(1).getPrice());
        assertEquals("John", orders.get(0).getEmployees().get(0).getFirstName());
        assertEquals("Smith", orders.get(0).getEmployees().get(0).getLastName());
        assertEquals("Cook", orders.get(0).getEmployees().get(0).getJob().getName());

        assertEquals(30.99, orders.get(1).getPrice());
        assertEquals("2022-08-22", orders.get(1).getDate());
        assertEquals("12:05", orders.get(1).getHourOrder());
        assertNull(orders.get(1).getHourAway());
        assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", orders.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, orders.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", orders.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, orders.get(1).getMenuItems().get(1).getPrice());
        assertEquals("John", orders.get(1).getEmployees().get(0).getFirstName());
        assertEquals("Smith", orders.get(1).getEmployees().get(0).getLastName());
        assertEquals("Cook", orders.get(1).getEmployees().get(0).getJob().getName());
    }
}
