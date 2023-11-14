package pl.jakubtworek.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderControllerE2ETest extends AbstractIT {

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(1, Map.of("typeOfOrder", "DELIVERY")),
                Arguments.of(2, Map.of("fromDate", createFromDateStr(), "toDate", createToDateStr())),
                Arguments.of(0, Map.of("isReady", "true")),
                Arguments.of(0, Map.of("employeeId", "1")),
                Arguments.of(2, Map.of("userId", "1"))
        );
    }

    private static String createToDateStr() {
        final var toDate = ZonedDateTime.now().plusHours(1);
        return DateTimeFormatter.ISO_INSTANT.format(toDate);
    }

    private static String createFromDateStr() {
        final var fromDate = ZonedDateTime.now().minusHours(2);
        return DateTimeFormatter.ISO_INSTANT.format(fromDate);
    }

    @Test
    @DirtiesContext
    void shouldCreateOrder() {
        // given
        final var request = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );

        // when
        final var response = postOrder(request, userToken);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().id());
    }

    @Test
    @DirtiesContext
    void shouldGetAllOrders() {
        // given
        final var request1 = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "ON_SITE",
                List.of("Pasta", "Sprite")
        );
        postOrder(request1, userToken);
        postOrder(request2, userToken);

        // when
        final var response = getOrders(userToken);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var orders = List.of(response.getBody());
        assertEquals(2, orders.size());
    }

    @Test
    @DirtiesContext
    void gouldGetOrderById() {
        // given
        final var request = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );
        final var createdId = postOrder(request, userToken)
                .getBody().id();

        // when
        final var response = getOrderById(createdId, userToken);

        // ten
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TypeOfOrder.DELIVERY, response.getBody().typeOfOrder());
    }

    @ParameterizedTest(name = "Orders: {0}, Parameters: {1}")
    @MethodSource("parameters")
    @DirtiesContext
    void shouldGetOrdersByParams(int expectedAmountOfOrders, Map<String, String> params) {
        // given
        final var request1 = new OrderRequest(
                "DELIVERY",
                List.of("Burger", "Cola")
        );
        final var request2 = new OrderRequest(
                "ON_SITE",
                List.of("Pasta", "Sprite")
        );
        postOrder(request1, userToken);
        postOrder(request2, userToken);

        // when
        final var response = getOrderByParam(userToken, params);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var orders = List.of(response.getBody());
        assertEquals(expectedAmountOfOrders, orders.size());
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
        final var createdOrder2 = postOrder(request2, userToken);
        final var createdOrder1 = postOrder(request1, userToken);

        final var cook1 = postEmployee(new EmployeeRequest("John", "Doe", "COOK")).getBody();
        final var cook2 = postEmployee(new EmployeeRequest("Mary", "Smith", "COOK")).getBody();
        final var waiter = postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER")).getBody();
        final var delivery = postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY")).getBody();

        // then
        assertEquals(HttpStatus.CREATED, createdOrder1.getStatusCode());
        assertEquals(HttpStatus.CREATED, createdOrder2.getStatusCode());
        assertNotNull(createdOrder1.getBody().id());
        assertNotNull(createdOrder2.getBody().id());

        Thread.sleep(2000);

        final var response = getOrders(userToken);
        List<OrderDto> orders = List.of(response.getBody());
        final var onsiteOrderResponse = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.ON_SITE).findFirst().get();
        final var deliveryOrderResponse = orders.stream().filter(o -> o.getTypeOfOrder() == TypeOfOrder.DELIVERY).findFirst().get();
        final var cook1Order = Arrays.stream(Objects.requireNonNull(getOrderByParam(userToken, Map.of("employeeId", String.valueOf(cook1.getId()))).getBody())).sequential().findFirst().get();
        final var cook2Order = Arrays.stream(Objects.requireNonNull(getOrderByParam(userToken, Map.of("employeeId", String.valueOf(cook2.getId()))).getBody())).sequential().findFirst().get();
        final var waiterOrder = Arrays.stream(Objects.requireNonNull(getOrderByParam(userToken, Map.of("employeeId", String.valueOf(waiter.getId()))).getBody())).sequential().findFirst().get();
        final var deliveryOrder = Arrays.stream(Objects.requireNonNull(getOrderByParam(userToken, Map.of("employeeId", String.valueOf(delivery.getId()))).getBody())).sequential().findFirst().get();
        assertEquals(onsiteOrderResponse.getId(), cook1Order.getId());
        assertEquals(onsiteOrderResponse.getId(), waiterOrder.getId());
        assertEquals(deliveryOrderResponse.getId(), cook2Order.getId());
        assertEquals(deliveryOrderResponse.getId(), deliveryOrder.getId());
    }
}
