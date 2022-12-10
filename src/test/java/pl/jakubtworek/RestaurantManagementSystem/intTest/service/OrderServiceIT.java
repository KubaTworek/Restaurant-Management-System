package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

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
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest order = createOnsiteOrderRequest();

        // when
        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");

        OrderDTO orderCreated = orderService.save(order);
        List<OrderDTO> orders = orderService.findAll();

        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        // then
        assertEquals(12.98, orderCreated.getPrice());
        assertEquals(date.format(localDateTime), orderCreated.getDate());
        assertEquals(null, orderCreated.getHourAway());
        assertEquals("On-site", orderCreated.getTypeOfOrder().getType());
        assertEquals("Chicken", orderCreated.getMenuItems().get(0).getName());
        assertEquals(10.99, orderCreated.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderCreated.getMenuItems().get(1).getName());
        assertEquals(1.99, orderCreated.getMenuItems().get(1).getPrice());
        assertEquals(1, orders.size());
    }


    @Test
    void shouldReturnLowerSizeOfList_whenDeleteOne() throws OrderNotFoundException {
        // when
        orderService.deleteById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"));
        List<OrderDTO> orders = orderService.findAll();

        // then
        assertEquals(1, orders.size());
    }

    @Test
    void shouldReturnAllOrders() {
        // when
        List<OrderDTO> ordersReturned = orderService.findAll();

        // then
        OrderDTOAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOneOrder() {
        UUID generated = UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002");


        // when
        OrderDTO orderReturned = orderService.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        OrderDTOAssertions.checkAssertionsForOrder(orderReturned);
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassDate() {
        // given
        String date = "2022-08-22";

        // when
        List<OrderDTO> orders = orderService.findByDate(date);

        // then
        assertEquals(2, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassTypeOfOrder() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        List<OrderDTO> orders = orderService.findByTypeOfOrder(typeOfOrder);

        // then
        assertEquals(1, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassEmployee() {
        // when
        List<OrderDTO> orders = orderService.findByEmployeeId(1L);

        // then
        assertEquals(2, orders.size());
    }*/
}
