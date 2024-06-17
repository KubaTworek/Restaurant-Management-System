package pl.jakubtworek.orders;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.dto.EmployeeRequest;
import pl.jakubtworek.order.dto.AddressRequest;
import pl.jakubtworek.order.dto.OrderConfirmRequest;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderReceiveRequest;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.vo.DeliveryStatus;
import pl.jakubtworek.order.vo.OrderStatus;
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
    void happyFlow_deliveryOrderX() throws InterruptedException {
        postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // create order
        final var createRequest = new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"), new AddressRequest("OCHOTA", "TEST", "TEST"));
        final var created = postOrder(createRequest, userToken);
        final var createdId = created.getId();
        final var request2 = new OrderConfirmRequest(createdId, "ACCEPT");
        final var confirmed = confirmOrder(request2, userToken);
        assertNotNull(confirmed.getDelivery());

        // wait for all processes
        Thread.sleep(500);
        final var createRequestX = new OrderRequest("ON_SITE", List.of("Burger", "Cola", "Cola"), null);
        final var createdX = postOrder(createRequestX, userToken);
        final var createdIdX = createdX.getId();
        final var request2X = new OrderConfirmRequest(createdIdX, "ACCEPT");
        final var confirmedX = confirmOrder(request2X, userToken);
        assertNull(confirmedX.getDelivery());

        // wait for all processes
        Thread.sleep(500);
        final var createRequestY = new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"), new AddressRequest("OCHOTA", "TEST", "TEST"));
        final var createdY = postOrder(createRequestY, userToken);
        final var createdIdY = createdY.getId();
        final var request2Y = new OrderConfirmRequest(createdIdY, "ACCEPT");
        final var confirmedY = confirmOrder(request2Y, userToken);
        assertNotNull(confirmedY.getDelivery());

        final var onsite = getOrderById(createdX.getId(), userToken);
        assertNull(onsite.getDelivery());
    }

    @Test
    @DirtiesContext
    void happyFlow_deliveryOrder() throws InterruptedException {
        final var cook = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var delivery = postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // create order
        final var createRequest = new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"), new AddressRequest("OCHOTA", "TEST", "TEST"));
        final var created = postOrder(createRequest, userToken);
        final var createdId = created.getId();
        assertEquals(TypeOfOrder.DELIVERY, created.getTypeOfOrder());
        assertEquals(OrderStatus.NEW, created.getStatus());
        assertEquals(BigDecimal.valueOf(22.97), created.getPrice().getPrice());
        assertEquals(3.00, created.getPrice().getDeliveryFee().doubleValue());
        assertEquals(BigDecimal.valueOf(4.03), created.getPrice().getMinimumBasketFee());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getTip());
        assertNotNull(created.getHourOrder());
        assertNull(created.getHourPrepared());
        assertNull(created.getHourReceived());
        final var burger = created.getOrderItems().stream().filter(orderItem -> "Burger".equals(orderItem.getName())).findFirst().get();
        assertEquals("Burger", burger.getName());
        assertEquals(1, burger.getAmount());
        assertEquals(BigDecimal.valueOf(10.99), burger.getPrice());
        final var cola = created.getOrderItems().stream().filter(orderItem -> "Cola".equals(orderItem.getName())).findFirst().get();
        assertEquals("Cola", cola.getName());
        assertEquals(2, cola.getAmount());
        assertEquals(BigDecimal.valueOf(5.99), cola.getPrice());
        final var deliveryDto = created.getDelivery();
        assertEquals("OCHOTA", deliveryDto.getDistrict().name());
        assertEquals("TEST", deliveryDto.getStreet());
        assertEquals("TEST", deliveryDto.getHouseNumber());
        assertNull(deliveryDto.getHourStart());
        assertNull(deliveryDto.getHourEnd());
        assertNull(deliveryDto.getStatus());

        // confirm order
        final var request2 = new OrderConfirmRequest(created.getId(), "ACCEPT");
        final var confirmed = confirmOrder(request2, userToken);
        assertEquals(createdId, confirmed.getId());
        assertEquals(OrderStatus.ACCEPT, confirmed.getStatus());
        assertNotNull(confirmed.getHourOrder());
        assertNull(confirmed.getHourPrepared());
        assertNull(confirmed.getHourReceived());

        // wait for all processes
        Thread.sleep(1000);

        // receive order and leave tip
        final var receiveRequest = new OrderReceiveRequest(created.getId(), BigDecimal.valueOf(2.00));
        final var received = receiveOrder(receiveRequest, userToken);
        assertEquals(createdId, received.getId());
        assertEquals(BigDecimal.valueOf(2.00), received.getPrice().getTip());
        assertEquals(OrderStatus.COMPLETED, received.getStatus());
        assertNotNull(received.getHourOrder());
        assertNotNull(received.getHourPrepared());
        assertNotNull(received.getHourReceived());
        final var deliveryReceived = received.getDelivery();
        assertNotNull(deliveryReceived.getHourStart());
        assertNotNull(deliveryReceived.getHourEnd());
        assertEquals(DeliveryStatus.END, deliveryReceived.getStatus());

        assertCookAndDeliveryAssignment(cook, delivery);
    }

    @Test
    @DirtiesContext
    void happyFlow_onsiteOrder() throws InterruptedException {
        final var cook = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));

        // create order
        final var createRequest = new OrderRequest("ON_SITE", List.of("Burger", "Cola", "Cola"), null);
        final var created = postOrder(createRequest, userToken);
        final var createdId = created.getId();
        assertEquals(TypeOfOrder.ON_SITE, created.getTypeOfOrder());
        assertEquals(OrderStatus.NEW, created.getStatus());
        assertEquals(BigDecimal.valueOf(22.97), created.getPrice().getPrice());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getDeliveryFee());
        assertEquals(BigDecimal.valueOf(7.03), created.getPrice().getMinimumBasketFee());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getTip());
        assertNotNull(created.getHourOrder());
        assertNull(created.getHourPrepared());
        assertNull(created.getHourReceived());
        final var burger = created.getOrderItems().stream().filter(orderItem -> "Burger".equals(orderItem.getName())).findFirst().get();
        assertEquals("Burger", burger.getName());
        assertEquals(1, burger.getAmount());
        assertEquals(BigDecimal.valueOf(10.99), burger.getPrice());
        final var cola = created.getOrderItems().stream().filter(orderItem -> "Cola".equals(orderItem.getName())).findFirst().get();
        assertEquals("Cola", cola.getName());
        assertEquals(2, cola.getAmount());
        assertEquals(BigDecimal.valueOf(5.99), cola.getPrice());

        // confirm order
        final var confirmRequest = new OrderConfirmRequest(createdId, "ACCEPT");
        final var confirmed = confirmOrder(confirmRequest, userToken);
        assertEquals(createdId, confirmed.getId());
        assertEquals(OrderStatus.ACCEPT, confirmed.getStatus());
        assertNotNull(confirmed.getHourOrder());
        assertNull(confirmed.getHourPrepared());
        assertNull(confirmed.getHourReceived());

        // wait for all processes
        Thread.sleep(1000);

        // receive order and not leave tip
        final var receiveRequest = new OrderReceiveRequest(createdId, null);
        final var received = receiveOrder(receiveRequest, userToken);
        assertEquals(createdId, received.getId());
        assertEquals(BigDecimal.valueOf(22.97), created.getPrice().getPrice());
        assertEquals(OrderStatus.COMPLETED, received.getStatus());
        assertNotNull(received.getHourOrder());
        assertNotNull(received.getHourPrepared());
        assertNotNull(received.getHourReceived());

        assertCookAssignment(cook);
    }

    @Test
    @DirtiesContext
    void badFlow_notAccept() throws InterruptedException {
        postEmployee(new EmployeeRequest("John", "Doe", "COOK"));

        // create order
        final var createRequest = new OrderRequest("ON_SITE", List.of("Burger", "Cola", "Cola"), null);
        final var created = postOrder(createRequest, userToken);
        final var createdId = created.getId();
        assertEquals(TypeOfOrder.ON_SITE, created.getTypeOfOrder());
        assertEquals(OrderStatus.NEW, created.getStatus());
        assertEquals(BigDecimal.valueOf(22.97), created.getPrice().getPrice());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getDeliveryFee());
        assertEquals(BigDecimal.valueOf(7.03), created.getPrice().getMinimumBasketFee());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getTip());
        assertNotNull(created.getHourOrder());
        assertNull(created.getHourPrepared());
        assertNull(created.getHourReceived());
        final var burger = created.getOrderItems().stream().filter(orderItem -> "Burger".equals(orderItem.getName())).findFirst().get();
        assertEquals("Burger", burger.getName());
        assertEquals(1, burger.getAmount());
        assertEquals(BigDecimal.valueOf(10.99), burger.getPrice());
        final var cola = created.getOrderItems().stream().filter(orderItem -> "Cola".equals(orderItem.getName())).findFirst().get();
        assertEquals("Cola", cola.getName());
        assertEquals(2, cola.getAmount());
        assertEquals(BigDecimal.valueOf(5.99), cola.getPrice());

        // confirm order
        final var confirmRequest = new OrderConfirmRequest(createdId, "REJECT");
        final var confirmed = confirmOrder(confirmRequest, userToken);
        assertEquals(createdId, confirmed.getId());
        assertEquals(OrderStatus.CANCELLED, confirmed.getStatus());
        assertNotNull(confirmed.getHourOrder());
        assertNull(confirmed.getHourPrepared());
        assertNull(confirmed.getHourReceived());

        // wait for all processes
        Thread.sleep(1000);

        // get cancelled order
        final var cancelled = getOrderById(createdId, userToken);
        assertEquals(createdId, cancelled.getId());
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
        assertNotNull(cancelled.getHourOrder());
        assertNull(cancelled.getHourPrepared());
        assertNull(cancelled.getHourReceived());
    }

    @Test
    @DirtiesContext
    void badFlow_NotReceivedBeforeSpecificTime() throws InterruptedException {
        final var cook = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var delivery = postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // create order
        final var request1 = new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"), new AddressRequest("SRODMIESCIE", "TEST", "TEST"));
        final var created = postOrder(request1, userToken);
        final var createdId = created.getId();
        assertEquals(TypeOfOrder.DELIVERY, created.getTypeOfOrder());
        assertEquals(OrderStatus.NEW, created.getStatus());
        assertEquals(BigDecimal.valueOf(22.97), created.getPrice().getPrice());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getDeliveryFee());
        assertEquals(BigDecimal.valueOf(7.03), created.getPrice().getMinimumBasketFee());
        assertEquals(BigDecimal.valueOf(0), created.getPrice().getTip());
        assertNotNull(created.getHourOrder());
        assertNull(created.getHourPrepared());
        assertNull(created.getHourReceived());
        final var burger = created.getOrderItems().stream().filter(orderItem -> "Burger".equals(orderItem.getName())).findFirst().get();
        assertEquals("Burger", burger.getName());
        assertEquals(1, burger.getAmount());
        assertEquals(BigDecimal.valueOf(10.99), burger.getPrice());
        final var cola = created.getOrderItems().stream().filter(orderItem -> "Cola".equals(orderItem.getName())).findFirst().get();
        assertEquals("Cola", cola.getName());
        assertEquals(2, cola.getAmount());
        assertEquals(BigDecimal.valueOf(5.99), cola.getPrice());
        final var deliveryDto = created.getDelivery();
        assertEquals("SRODMIESCIE", deliveryDto.getDistrict().name());
        assertEquals("TEST", deliveryDto.getStreet());
        assertEquals("TEST", deliveryDto.getHouseNumber());
        assertNull(deliveryDto.getHourStart());
        assertNull(deliveryDto.getHourEnd());
        assertNull(deliveryDto.getStatus());

        // confirm order
        final var confirmRequest = new OrderConfirmRequest(createdId, "ACCEPT");
        final var confirmed = confirmOrder(confirmRequest, userToken);
        assertEquals(createdId, confirmed.getId());
        assertEquals(OrderStatus.ACCEPT, confirmed.getStatus());
        assertNotNull(confirmed.getHourOrder());
        assertNull(confirmed.getHourPrepared());
        assertNull(confirmed.getHourReceived());

        // wait for all processes
        Thread.sleep(2000);

        // get cancelled order
        final var cancelled = getOrderById(createdId, userToken);
        assertEquals(createdId, cancelled.getId());
        assertEquals(OrderStatus.CANCELLED, cancelled.getStatus());
        assertNotNull(cancelled.getHourOrder());
        assertNotNull(cancelled.getHourPrepared());
        assertNull(cancelled.getHourReceived());
        final var deliveryCancelled = cancelled.getDelivery();
        assertNotNull(deliveryCancelled.getHourStart());
        assertNotNull(deliveryCancelled.getHourEnd());
        assertEquals(DeliveryStatus.END, deliveryCancelled.getStatus());

        assertCookAndDeliveryAssignment(cook, delivery);
    }

    @Test
    @DirtiesContext
    void shouldVerifyIsOrderAreComparedAndIsCookAssigned_whenThereIsMoreThanOneOrderInQueue() throws InterruptedException {
        // given
        final var delivery = postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola"), new AddressRequest("SRODMIESCIE", "TEST", "TEST")), userToken);
        final var onSite = postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite"), null), userToken);
        confirmOrder(new OrderConfirmRequest(delivery.getId(), "ACCEPT"), userToken);
        confirmOrder(new OrderConfirmRequest(onSite.getId(), "ACCEPT"), userToken);

        final var cook1 = postEmployee(new EmployeeRequest("John", "Doe", "COOK"));
        final var cook2 = postEmployee(new EmployeeRequest("Mary", "Smith", "COOK"));
        final var deliveryMan = postEmployee(new EmployeeRequest("Bob", "Dylan", "DELIVERY"));

        // when
        Thread.sleep(3000);

        // then
        assertCookAssignment(cook1);
        assertCookAndDeliveryAssignment(cook2, deliveryMan);
    }

    @Test
    @DirtiesContext
    void shouldGetAllOrders() {
        // given
        postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola"), new AddressRequest("SRODMIESCIE", "TEST", "TEST")), userToken);
        postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite"), null), userToken);

        // when
        final var response = getOrders(userToken);

        // then
        assertEquals(2, response.size());
    }

    @Test
    @DirtiesContext
    void shouldGetOrderById() {
        // given
        final var createdId = postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola", "Cola"), new AddressRequest("SRODMIESCIE", "TEST", "TEST")), userToken).getId();

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
        postOrder(new OrderRequest("DELIVERY", List.of("Burger", "Cola"), new AddressRequest("SRODMIESCIE", "TEST", "TEST")), userToken);
        postOrder(new OrderRequest("ON_SITE", List.of("Pasta", "Sprite"), null), userToken);

        // when
        final var response = getOrderByParam(params);

        // then
        assertEquals(expectedAmountOfOrders, response.size());
    }

    private void assertOrderResponse(OrderDto response) {
        assertEquals(TypeOfOrder.DELIVERY, response.getTypeOfOrder());
        assertEquals(BigDecimal.valueOf(22.97), response.getPrice().getPrice());
        assertEquals(0, response.getPrice().getDeliveryFee().compareTo(BigDecimal.ZERO));
        assertEquals(BigDecimal.valueOf(7.03), response.getPrice().getMinimumBasketFee());
        assertEquals(0, response.getPrice().getTip().compareTo(BigDecimal.ZERO));
        assertNotNull(response.getHourOrder());
        assertNull(response.getHourPrepared());
        final var cola = response.getOrderItems().stream().filter(menuItem -> "Cola".equals(menuItem.getName())).findFirst().get();
        assertEquals(new BigDecimal("5.99"), cola.getPrice());
        assertEquals(2, cola.getAmount());
        final var burger = response.getOrderItems().stream().filter(menuItem -> "Burger".equals(menuItem.getName())).findFirst().get();
        assertEquals(new BigDecimal("10.99"), burger.getPrice());
        assertEquals(1, burger.getAmount());
    }

    private void assertCookAssignment(EmployeeDto cook) {
        final var response = getOrders(userToken)
                .stream()
                .filter(o -> getOrderByParam(Map.of("employeeId", String.valueOf(cook.getId())))
                        .stream().map(OrderDto::getId).toList().contains(o.getId()))
                .toList();

        assertEquals(1, response.size());
    }

    private void assertCookAndDeliveryAssignment(EmployeeDto cook, EmployeeDto delivery) {
        final var response = getOrders(userToken)
                .stream()
                .filter(o -> o.getId().equals(getOrderByParam(Map.of("employeeId", String.valueOf(cook.getId())))
                        .stream().findFirst().orElse(null).getId()))
                .toList();
        assertEquals(1, response.size());

/*        assertEquals(response.getId(), getOrderByParam(Map.of("employeeId", String.valueOf(delivery.getId())))
                .stream().findFirst().orElse(null).getId());*/
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
