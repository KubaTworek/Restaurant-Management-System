/*
package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderRepositoryIT {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllOrders() {
        // when
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(2, orders.size());

        assertEquals(1, orders.get(0).getId());
        assertEquals(12.99, orders.get(0).getPrice());
        assertEquals("2022-08-22", orders.get(0).getDate());
        assertEquals("12:00", orders.get(0).getHourOrder());
        assertEquals("12:15", orders.get(0).getHourAway());
        assertEquals(1, orders.get(0).getTypeOfOrder().getId());
        assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
        assertEquals(1, orders.get(0).getEmployees().size());
        assertEquals(2, orders.get(0).getMenuItems().size());

        assertEquals(2, orders.get(1).getId());
        assertEquals(30.99, orders.get(1).getPrice());
        assertEquals("2022-08-22", orders.get(1).getDate());
        assertEquals("12:05", orders.get(1).getHourOrder());
        assertNull(orders.get(1).getHourAway());
        assertEquals(2, orders.get(1).getTypeOfOrder().getId());
        assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
        assertEquals(1, orders.get(1).getEmployees().size());
        assertEquals(2, orders.get(1).getMenuItems().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneOrder() {
        // when
        Optional<Order> order = orderRepository.findById(1L);

        // then
        assertNotNull(order.get());
        assertEquals(1, order.get().getId());
        assertEquals(12.99, order.get().getPrice());
        assertEquals("2022-08-22", order.get().getDate());
        assertEquals("12:00", order.get().getHourOrder());
        assertEquals("12:15", order.get().getHourAway());
        assertEquals(1, order.get().getTypeOfOrder().getId());
        assertEquals("On-site", order.get().getTypeOfOrder().getType());
        assertEquals(1, order.get().getEmployees().size());
        assertEquals(2, order.get().getMenuItems().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnCreatedOrder() {
        // given
        Order order = createOnsiteOrder().get();

        // when
        Order orderReturned = orderRepository.save(order);

        // then
        assertEquals(12.99, orderReturned.getPrice());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals(2, orderReturned.getMenuItems().size());
    }

*/
/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        orderRepository.deleteById(1L);
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(1, orders.size());
    }*//*


*/
/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassDate() {
        // given
        String date = "2022-08-22";

        // when
        List<Order> orders = orderRepository.findByDate(date)
                .stream()
                .collect(Collectors.toList());

        // then
        assertEquals(2, orders.size());
    }*//*


    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassTypeOfOrder() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        List<Order> orders = orderRepository.findByTypeOfOrder(typeOfOrder)
                .get()
                .stream()
                .collect(Collectors.toList());

        // then
        assertEquals(1, orders.size());
    }

*/
/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassEmployee() {
        // given
        Employee employee = createEmployee().get();

        // when
        List<Order> orders = orderRepository.findByEmployees(List.of(employee))
                .stream()
                .collect(Collectors.toList());

        // then
        assertEquals(2, orders.size());
    }*//*

}
*/
