package pl.jakubtworek.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class OrderControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateOrder() {
        // given
        final var request = new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"));

        // when
        final var response = postOrder(request, userToken);

        // then
        assertOrderResponse(response);
    }

    @Test
    @DirtiesContext
    void shouldGetAllOrders() {
        // given
        postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola")), userToken);
        postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite")), userToken);

        // when
        final var response = getOrders(userToken);

        // then
        assertEquals(2, response.size());
    }

    @Test
    @DirtiesContext
    void shouldGetOrderById() {
        // given
        final var createdId = postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola")), userToken).getId();

        // when
        final var response = getOrderById(createdId, userToken);

        // then
        assertOrderResponse(response);
    }

    @ParameterizedTest(name = "Orders: {0}, Parameters: {1}")
    @MethodSource("parameters")
    @DirtiesContext
    void shouldGetOrdersByParams(int expectedAmountOfOrders, Map<String, String> params) {
        // given
        postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola")), userToken);
        postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite")), userToken);

        // when
        final var response = getOrderByParam(params);

        // then
        assertEquals(expectedAmountOfOrders, response.size());
    }

    @Test
    @DirtiesContext
    void shouldCookTheOtherCook_whenThereIsMoreThanOneOrderInQueue() throws InterruptedException {
        // given
        postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola")), userToken);
        postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite")), userToken);

        final var cook1 = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var cook2 = postEmployee(new EmployeeRequest("Mary", "Smith", "COOK"));
        final var waiter = postEmployee(new EmployeeRequest("Jane", "Smith", "WAITER"));
        final var delivery = postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // when
        Thread.sleep(2000);

        // then
        assertCookAssignment(cook1, waiter);
        assertCookAssignment(cook2, delivery);
    }

    private void assertOrderResponse(OrderDto response) {
        assertEquals(TypeOfOrder.DELIVERY, response.getTypeOfOrder());
        assertEquals(new BigDecimal("22.97"), response.getPrice());
        assertNotNull(response.getHourOrder());
        assertNull(response.getHourAway());
        final var cola = response.getOrderItems().stream().filter(menuItem -> "Cola".equals(menuItem.getName())).findFirst().get();
        assertEquals(new BigDecimal("5.99"), cola.getPrice());
        assertEquals(2, cola.getAmount());
        final var burger = response.getOrderItems().stream().filter(menuItem -> "Burger".equals(menuItem.getName())).findFirst().get();
        assertEquals(new BigDecimal("10.99"), burger.getPrice());
        assertEquals(1, burger.getAmount());
    }

    private void assertCookAssignment(EmployeeDto cook, EmployeeDto other) {
        final var response = getOrders(userToken)
                .stream()
                .filter(o -> o.getId().equals(getOrderByParam(Map.of("employeeId", String.valueOf(cook.getId())))
                        .stream().findFirst().orElse(null).getId()))
                .findFirst()
                .orElse(null);

        assertEquals(response.getId(), getOrderByParam(Map.of("employeeId", String.valueOf(other.getId())))
                .stream().findFirst().orElse(null).getId());
    }

    private static Stream<Arguments> parameters() {
        return Stream.of(
                Arguments.of(1, Map.of("typeOfOrder", "DELIVERY")),
                Arguments.of(2, Map.of("fromDate", createFromDateStr(), "toDate", createToDateStr())),
                Arguments.of(0, Map.of("isReady", "true")),
                Arguments.of(0, Map.of("employeeId", "1")),
                Arguments.of(2, Map.of("customerId", "1"))
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
}
