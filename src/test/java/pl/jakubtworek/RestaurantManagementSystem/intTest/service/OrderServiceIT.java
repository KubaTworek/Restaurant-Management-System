package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.createOnsiteOrderRequest;

@SpringBootTest
@Transactional
class OrderServiceIT {

    @Autowired
    private OrderService orderService;

    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllOrders() {
        // when
        List<OrderDTO> orders = orderService.findAll();

        // then
        assertEquals(2, orders.size());

        assertEquals(12.99, orders.get(0).getPrice());
        assertEquals("2022-08-22", orders.get(0).getDate());
        assertEquals("12:00", orders.get(0).getHourOrder());
        assertEquals("12:15", orders.get(0).getHourAway());
        assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
        assertEquals(1, orders.get(0).getEmployees().size());
        assertEquals(2, orders.get(0).getMenuItems().size());

        assertEquals(30.99, orders.get(1).getPrice());
        assertEquals("2022-08-22", orders.get(1).getDate());
        assertEquals("12:05", orders.get(1).getHourOrder());
        assertNull(orders.get(1).getHourAway());
        assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
        assertEquals(1, orders.get(1).getEmployees().size());
        assertEquals(2, orders.get(1).getMenuItems().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneOrder() {
        // when
        Optional<OrderDTO> order = orderService.findById(1L);

        // then
        assertEquals(12.99, order.get().getPrice());
        assertEquals("2022-08-22", order.get().getDate());
        assertEquals("12:00", order.get().getHourOrder());
        assertEquals("12:15", order.get().getHourAway());
        assertEquals("On-site", order.get().getTypeOfOrder().getType());
        assertEquals(1, order.get().getEmployees().size());
        assertEquals(2, order.get().getMenuItems().size());
    }

    @Test
    @Sql(statements = {"INSERT INTO `type_of_order` VALUES (1,'On-site'), (2,'Delivery')", "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')", "INSERT INTO `menu_item`(id,name,price,menu_id) VALUES (1,'Chicken',10.99,2), (2,'Coke',1.99,1), (3,'Tiramisu',5.99,2)", "INSERT INTO `authorities`(`id`, `authority`) VALUES (1, 'admin'), (2, 'user')", "INSERT INTO `users`(`id`, `username`, `password`, `role_id`) VALUES (1, 'admin', 'admin', 1), (2, 'user', 'user', 2)"})
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest order = createOnsiteOrderRequest();

        // when
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        OrderDTO orderReturned = orderService.save(order);

        // then
        assertEquals(12.98, orderReturned.getPrice());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals(2, orderReturned.getMenuItems().size());
    }


    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() throws OrderNotFoundException {
        // when
        orderService.deleteById(1L);
        List<OrderDTO> orders = orderService.findAll();

        // then
        assertEquals(1, orders.size());
    }


/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassDate() {
        // given
        String date = "2022-08-22";

        // when
        List<OrderDTO> orders = orderService.findByDate(date);

        // then
        assertEquals(2, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassTypeOfOrder() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        List<OrderDTO> orders = orderService.findByTypeOfOrder(typeOfOrder);

        // then
        assertEquals(1, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenPassEmployee() {
        // when
        List<OrderDTO> orders = orderService.findByEmployeeId(1L);

        // then
        assertEquals(2, orders.size());
    }*/
}
