package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.createOnsiteOrder;

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

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        orderRepository.deleteById(1L);
        List<Order> orders = orderRepository.findAll();

        // then
        assertEquals(1, orders.size());
    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassDate() {
        // when
        List<Order> orders = orderRepository.findByDate("2022-08-22");

        // then
        assertEquals(2, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassTypeOfOrder() {
        // when
        List<Order> orders = orderRepository.findByTypeOfOrder(spy(new TypeOfOrder(1L, "On-site", List.of())));

        // then
        assertEquals(1, orders.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOrders_whenPassEmployee() {
        // when
        List<Order> orders = orderRepository.findByEmploye(spy(new Employee(1L, "John", "Smith", new Job(1L,"Cook",List.of()), List.of())));

        // then
        assertEquals(2, orders.size());
    }
}
