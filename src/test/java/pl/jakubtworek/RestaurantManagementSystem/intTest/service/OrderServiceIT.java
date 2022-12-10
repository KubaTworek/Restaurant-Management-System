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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

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
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
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

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllOrders() {
        // when
        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneOrder() {
        // when
        OrderDTO orderReturned = orderService.findById(1L).orElse(null);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
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
