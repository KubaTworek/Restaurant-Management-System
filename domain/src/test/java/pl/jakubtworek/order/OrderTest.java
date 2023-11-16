package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class OrderTest {
    @Test
    void shouldRestoreOrderFromSnapshot() {
        // given
        final var snapshot = new OrderSnapshot(1L, new BigDecimal("25.99"), ZonedDateTime.now(), null, TypeOfOrder.DELIVERY, new HashSet<>(), new HashSet<>(), new UserId(1L));

        // when
        final var order = Order.restore(snapshot, 1);

        // then
        final var result = order.getSnapshot(1);
        assertEquals(snapshot.getId(), result.getId());
        assertEquals(snapshot.getPrice(), result.getPrice());
        assertEquals(snapshot.getHourOrder(), result.getHourOrder());
        assertNull(result.getHourAway());
        assertEquals(snapshot.getTypeOfOrder(), result.getTypeOfOrder());
        assertEquals(snapshot.getOrderItems(), result.getOrderItems());
        assertEquals(snapshot.getEmployees(), result.getEmployees());
        assertEquals(snapshot.getClientId(), result.getClientId());
    }

    @Test
    void shouldAddEmployeeToOrder() {
        // given
        final var order = new Order();
        final var employeeId = new EmployeeId(1L);

        // when
        order.addEmployee(employeeId);

        // then
        assertTrue(order.getSnapshot(1).getEmployees().contains(employeeId));
    }

    @Test
    void shouldDeliveryUpdatesHourAway() {
        // given
        final var order = new Order();

        // when
        order.delivery();

        // then
        assertNotNull(order.getSnapshot(1).getHourAway());
    }

    @Test
    void shouldUpdateOrderInfo() {
        // given
        final var order = new Order();
        final Set<OrderItem> menuItems = new HashSet<>();
        final var price = new Money(new BigDecimal("30.50"));
        final var typeOfOrderName = "DELIVERY";
        final var user = new UserId(1L);

        // when
        order.updateInfo(menuItems, price, typeOfOrderName, user);

        // then
        final var result = order.getSnapshot(1);
        assertEquals(menuItems, result.getOrderItems());
        assertEquals(price.getValue(), result.getPrice());
        assertNotNull(result.getHourOrder());
        assertEquals(TypeOfOrder.DELIVERY, result.getTypeOfOrder());
        assertEquals(user, result.getClientId());
    }

/*    @Test
    void shouldGetAmountOfMenuItems() {
        // given
        final var order = new Order();
        final Set<OrderItem> menuItems = new HashSet<>();
        menuItems.add(new OrderItem(1L, new OrderId(1L), new MenuItemId(1L)));
        menuItems.add(new OrderItem(2L, new OrderId(1L), new MenuItemId(2L)));
        order.updateInfo(menuItems, new Money(new BigDecimal("16.98")), "ON_SITE", new UserId(1L));

        // when & then
        assertEquals(2, order.getAmountOfMenuItems());
    }*/
}
