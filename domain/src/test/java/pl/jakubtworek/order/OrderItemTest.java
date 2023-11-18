package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemTest {

    @Test
    void shouldRestoreOrderItemFromSnapshot_whenDepthIsZero() {
        // given
        final var orderItemSnapshot = new OrderItemSnapshot(1L, "Burger", BigDecimal.valueOf(10), 2, null);

        // when
        final var orderItem = OrderItem.restore(orderItemSnapshot, 0);

        // then
        assertOrderItemSnapshotsEqual(orderItemSnapshot, orderItem.getSnapshot(1));
    }

    @Test
    void shouldRestoreOrderItemFromSnapshot_whenDepthIsGreaterThanZero() {
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
        final var name = "Burger";
        final var amount = 2;

        // when
        orderItem.updateInfo(name, createItems(), amount);

        // then
        final var result = orderItem.getSnapshot(1);
        assertEquals(name, result.getName());
        assertEquals(BigDecimal.valueOf(10.99), result.getPrice());
        assertEquals(amount, result.getAmount());
    }

    @Test
    void shouldCalculatePriceForOrderItem() {
        // given
        final var orderItem = new OrderItem();
        orderItem.updateInfo("Burger", createItems(), 3);

        // when
        final var calculatedPrice = orderItem.calculatePrice();

        // then
        assertEquals(BigDecimal.valueOf(32.97), calculatedPrice);
    }

    private void assertOrderItemSnapshotsEqual(OrderItemSnapshot expected, OrderItemSnapshot actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
        assertEquals(expected.getAmount(), actual.getAmount());
        assertEquals(expected.getOrder(), actual.getOrder());
    }

    private Set<ItemDto> createItems() {
        final Set<ItemDto> items = new HashSet<>();
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        items.add(new ItemDto("Pizza", BigDecimal.valueOf(12.99)));
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        return items;
    }
}
