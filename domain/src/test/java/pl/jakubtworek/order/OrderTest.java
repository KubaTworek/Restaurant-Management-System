package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class OrderTest {

    @Test
    void shouldRestoreOrderFromSnapshotWithDepthZero() {
        // given
        final var orderSnapshot = new OrderSnapshot(1L, BigDecimal.valueOf(50), ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                TypeOfOrder.ON_SITE, Collections.emptySet(), new HashSet<>(), new UserId(1L));

        // when
        final var order = Order.restore(orderSnapshot, 0);

        // then
        assertOrderSnapshotsEqual(orderSnapshot, order.getSnapshot(1));
    }

    @Test
    void shouldRestoreOrderFromSnapshotWithDepthGreaterThanZero() {
        // given
        final var orderItemSnapshot = new OrderItemSnapshot(1L, "Burger", BigDecimal.valueOf(10), 2, null);
        final var orderSnapshot = new OrderSnapshot(1L, BigDecimal.valueOf(50), ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                TypeOfOrder.ON_SITE, Collections.singleton(orderItemSnapshot), new HashSet<>(), new UserId(1L));

        // when
        final var order = Order.restore(orderSnapshot, 1);

        // then
        assertOrderSnapshotsEqual(orderSnapshot, order.getSnapshot(1));
    }

    @ParameterizedTest
    @CsvSource({"ON_SITE", "TAKE_AWAY", "DELIVERY"})
    void shouldUpdateInfoForOrder(String typeOfOrder) {
        // given
        final var order = new Order();
        final var userId = new UserId(1L);
        final var orderItems = createOrderItems();

        // when
        order.updateInfo(orderItems, typeOfOrder, userId);

        // then
        final var snap = order.getSnapshot(1);
        assertEquals(BigDecimal.valueOf(60), snap.getPrice());
        assertEquals(typeOfOrder, snap.getTypeOfOrder().toString());
        assertEquals(userId, snap.getClientId());
        assertNotNull(snap.getHourOrder());
        assertNull(snap.getHourAway());
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // given
        final var order = new Order();
        final var employeeId = new EmployeeId(1L);

        // when
        order.addEmployee(employeeId);

        // then
        assertEquals(Collections.singleton(employeeId), order.getSnapshot(1).getEmployees());
    }

    @Test
    void shouldSetDeliveryForOrder() {
        // given
        final var order = new Order();

        // when
        order.delivery();

        // then
        assertEquals(ZonedDateTime.now().getHour(), order.getSnapshot(1).getHourAway().getHour());
    }

    @Test
    void shouldReturnZeroAmountOfMenuItemsForEmptyOrder() {
        // given
        final var order = new Order();

        // when
        int amountOfMenuItems = order.getAmountOfMenuItems();

        // then
        assertEquals(0, amountOfMenuItems);
    }

    @Test
    void shouldReturnCorrectAmountOfMenuItems() {
        // given
        final var order = new Order();
        order.updateInfo(createOrderItems(), TypeOfOrder.ON_SITE.toString(), new UserId(1L));

        // when
        int amountOfMenuItems = order.getAmountOfMenuItems();

        // then
        assertEquals(3, amountOfMenuItems);
    }

    @Test
    void shouldThrowException_whenUpdateOrderInfoWithInvalidTypeOfOrder() {
        // given
        final var order = new Order();

        // when & then
        assertThrows(IllegalStateException.class, () ->
                order.updateInfo(createOrderItems(), "INVALID_TYPE_OF_ORDER_TYPE", new UserId(1L))
        );
    }

    private void assertOrderSnapshotsEqual(OrderSnapshot expected, OrderSnapshot actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getHourOrder(), actual.getHourOrder());
        assertEquals(expected.getHourAway(), actual.getHourAway());
        assertEquals(expected.getTypeOfOrder(), actual.getTypeOfOrder());
        assertEquals(expected.getOrderItems(), actual.getOrderItems());
        assertEquals(expected.getEmployees(), actual.getEmployees());
        assertEquals(expected.getClientId(), actual.getClientId());
    }

    private Set<OrderItem> createOrderItems() {
        final Set<OrderItem> orderItems = new HashSet<>();
        orderItems.add(OrderItem.restore(new OrderItemSnapshot(1L, "Item1", BigDecimal.TEN, 2, null), 0));
        orderItems.add(OrderItem.restore(new OrderItemSnapshot(2L, "Item2", BigDecimal.TEN, 1, null), 0));
        orderItems.add(OrderItem.restore(new OrderItemSnapshot(3L, "Item3", BigDecimal.TEN, 3, null), 0));
        return orderItems;
    }
}

class OrderItemTest {

    @Test
    void shouldRestoreOrderItemFromSnapshotWithDepthZero() {
        // given
        final var orderItemSnapshot = new OrderItemSnapshot(1L, "Burger", BigDecimal.valueOf(10), 2, null);

        // when
        final var orderItem = OrderItem.restore(orderItemSnapshot, 0);

        // then
        assertOrderItemSnapshotsEqual(orderItemSnapshot, orderItem.getSnapshot(1));
    }

    @Test
    void shouldRestoreOrderItemFromSnapshotWithDepthGreaterThanZero() {
        // given
        final var orderSnapshot = new OrderSnapshot(1L, BigDecimal.valueOf(50), ZonedDateTime.now(), ZonedDateTime.now().plusHours(1),
                TypeOfOrder.ON_SITE, Collections.emptySet(), new HashSet<>(), new UserId(1L));
        final var orderItemSnapshot = new OrderItemSnapshot(1L, "Burger", BigDecimal.valueOf(10), 2, orderSnapshot);

        // when
        final var orderItem = OrderItem.restore(orderItemSnapshot, 1);

        // then
        assertOrderItemSnapshotsEqual(orderItemSnapshot, orderItem.getSnapshot(1));
    }

    @Test
    void shouldUpdateInfoForOrderItem() {
        // given
        final var orderItem = new OrderItem();
        final var name = "UpdatedItem";
        final var price = new Money(BigDecimal.TEN);
        final var amount = 2;

        // when
        orderItem.updateInfo(name, price, amount);

        // then
        final var result = orderItem.getSnapshot(1);
        assertEquals(name, result.getName());
        assertEquals(price.value(), result.getPrice());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void shouldCalculatePriceForOrderItem() {
        // given
        final var orderItem = new OrderItem();
        orderItem.updateInfo("TestItem", new Money(BigDecimal.TEN), 3);

        // when
        final var calculatedPrice = orderItem.calculatePrice();

        // then
        assertEquals(BigDecimal.valueOf(30), calculatedPrice);
    }

    private void assertOrderItemSnapshotsEqual(OrderItemSnapshot expected, OrderItemSnapshot actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getOrder(), actual.getOrder());
    }
}
