package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
@Transactional
class OrderRepositoryIT {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    void shouldReturnCreatedOrder() {
        // given
        Order order = createOnsiteOrder();

        // when
        Order orderCreated = orderRepository.save(order);

        // then
        OrderAssertions.checkAssertionsForOrder(orderCreated);
    }

    @Test
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        orderRepository.deleteById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"));
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(1, orders.size());
    }

    @Test
    void shouldReturnAllOrders() {
        // when
        List<Order> ordersReturned = orderRepository.findAll();

        // then
        OrderAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOneOrder() {
        // when
        Order orderReturned = orderRepository.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        OrderAssertions.checkAssertionsForOrder(orderReturned);
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassDate() {
        // given
        String date = "2022-08-22";

        // when
        List<Order> orders = orderRepository.findByDate(date);

        // then
        checkAssertionsForOrders(ordersR);
    }


    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassTypeOfOrder() {
        // given
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        List<Order> ordersReturned = orderRepository.findByTypeOfOrder(typeOfOrder);

        // then
        checkAssertionsForOrders(ordersReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnOrders_whenPassEmployee() {
        // when
        List<Order> ordersReturned = orderRepository.findByEmployeesId(1L);

        // then
        checkAssertionsForOrders(ordersReturned);
    }*/

}
