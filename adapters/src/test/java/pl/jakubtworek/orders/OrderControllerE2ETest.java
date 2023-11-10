package pl.jakubtworek.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class OrderControllerE2ETest extends AbstractIT {

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
        assertNotNull(response.getBody().getId());
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
                .getBody().getId();

        // when
        final var response = getOrderById(createdId, userToken);

        // ten
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(TypeOfOrder.DELIVERY, response.getBody().getTypeOfOrder());
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
}
