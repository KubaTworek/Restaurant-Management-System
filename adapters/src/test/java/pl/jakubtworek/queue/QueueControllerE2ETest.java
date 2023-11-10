package pl.jakubtworek.queue;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QueueControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldEveryOrderBePreparedBySpecificEmployee() throws InterruptedException {
        // given
        final var employee1 = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var employee2 = postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));

        final var request1 = new OrderRequest(
                "ON_SITE",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "DELIVERY",
                List.of("Pasta", "Sprite")
        );

        // when
        final var createdOrder1 = postOrder(request1, userToken);
        final var createdOrder2 = postOrder(request2, userToken);

        // then
        assertEquals(HttpStatus.CREATED, createdOrder1.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdOrder2.getStatusCode());
        assertNotNull(createdOrder1.getBody().getId());
        assertNotNull(createdOrder2.getBody().getId());

        Thread.sleep(1000);

        assertOrdersByEmployee(userToken, employee1, createdOrder1.getBody().getId(), createdOrder2.getBody().getId());
        assertOrdersByEmployee(userToken, employee2, createdOrder1.getBody().getId());
    }

    @Test
    @DirtiesContext
    void shouldCookTheOtherCook_whenThereIsMoreThanOneOrderInQueue() throws InterruptedException {
        // given
        final var request1 = new OrderRequest(
                "ON_SITE",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "DELIVERY",
                List.of("Pasta", "Sprite")
        );

        // when
        final var createdOrder1 = postOrder(request1, userToken);
        final var createdOrder2 = postOrder(request2, userToken);

        postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        postEmployee(new EmployeeRequest("Mary", "Smith", "COOK"));
        postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));
        postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // then
        assertEquals(HttpStatus.CREATED, createdOrder1.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdOrder2.getStatusCode());
        assertNotNull(createdOrder1.getBody().getId());
        assertNotNull(createdOrder2.getBody().getId());

        Thread.sleep(2000);

        final var response = getOrders(userToken);
        List<OrderDto> orders = List.of(response.getBody());
        final var onsite = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.ON_SITE).findFirst().get();
        final var delivery = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.DELIVERY).findFirst().get();
/*        final var cookOnsite = onsite.getEmployees().stream().filter(e -> e.getJob() == Job.COOK).findFirst().get();
        final var cookDelivery = delivery.getEmployees().stream().filter(e -> e.getJob() == Job.COOK).findFirst().get();
        assertEquals(cookOnsite.getFirstName(), "John");
        assertEquals(cookOnsite.getLastName(), "Doe");
        assertEquals(cookDelivery.getFirstName(), "Mary");
        assertEquals(cookDelivery.getLastName(), "Smith");*/

    }

    @Test
    @DirtiesContext
    void shouldCookOnsiteOrderBeforeDeliveryAndShouldBeCookedBySpecificEmployee() throws InterruptedException {
        // given
        final var request1 = new OrderRequest(
                "ON_SITE",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "DELIVERY",
                List.of("Pasta", "Sprite")
        );

        // when
        final var createdOrder2 = postOrder(request2, userToken);
        final var createdOrder1 = postOrder(request1, userToken);

        postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));
        postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // then
        assertEquals(HttpStatus.CREATED, createdOrder2.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdOrder1.getStatusCode());
        assertNotNull(createdOrder2.getBody().getId());
        assertNotNull(createdOrder1.getBody().getId());

        Thread.sleep(2000);

        final var response = getOrders(userToken);
        List<OrderDto> orders = List.of(response.getBody());
        final var onsite = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.ON_SITE).findFirst().get();
        final var delivery = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.DELIVERY).findFirst().get();
        assertTrue(onsite.getHourAway().isBefore(delivery.getHourAway()));
/*        assertEquals(2, onsite.getEmployees().size());
        assertTrue(onsite.getEmployees().stream().allMatch(e -> e.getJob() == Job.COOK || e.getJob() == Job.WAITER));
        assertEquals(2, delivery.getEmployees().size());
        assertTrue(delivery.getEmployees().stream().allMatch(e -> e.getJob() == Job.COOK || e.getJob() == Job.DELIVERY));*/

    }

    private void assertOrdersByEmployee(String userToken, ResponseEntity<EmployeeDto> employee, Long... orderIds) {
        final var response = getOrderByParam(userToken, Map.of("employeeId", employee.getBody().getId().toString()));
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var orders = List.of(response.getBody());
        assertEquals(orderIds.length, orders.size());
        for (var orderId : orderIds) {
            assertTrue(orders.stream().anyMatch(orderDto -> orderDto.getId().equals(orderId)));
        }
    }
}
