package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.common.vo.Status;

import java.math.BigDecimal;
import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MenuItemTest {
    @Test
    void shouldRestoreMenuItemFromSnapshot() {
        // given
        final var snapshot = new MenuItemSnapshot(1L, "Burger", new BigDecimal("10.99"), null, Status.ACTIVE);

        // when
        final var menuItem = MenuItem.restore(snapshot, 0);

        // then
        final var result = menuItem.getSnapshot(1);
        assertEquals(snapshot.getId(), result.getId());
        assertEquals(snapshot.getName(), result.getName());
        assertEquals(snapshot.getPrice(), result.getPrice());
        assertNull(result.getMenu());
        assertEquals(snapshot.getStatus(), result.getStatus());
    }

    @Test
    void shouldRestoreMenuItemWithMenuFromSnapshot() {
        // Create a menu snapshot
        final var menuSnapshot = new MenuSnapshot(1L, "Main Menu", new HashSet<>());

        // Create a menu item snapshot with a menu reference
        final var menuItemSnapshot = new MenuItemSnapshot(1L, "Pizza", new BigDecimal("15.99"), menuSnapshot, Status.ACTIVE);

        // Restore the menu item
        final var menuItem = MenuItem.restore(menuItemSnapshot, 1);

        // Check assertions
        assertEquals(menuItemSnapshot.getId(), menuItem.getSnapshot(1).getId());
        assertEquals(menuItemSnapshot.getName(), menuItem.getSnapshot(1).getName());
        assertEquals(menuItemSnapshot.getPrice(), menuItem.getSnapshot(1).getPrice());
        assertNotNull(menuItem.getSnapshot(1).getMenu());
        assertEquals(menuItemSnapshot.getStatus(), menuItem.getSnapshot(1).getStatus());

        // Check assertions for the restored menu
        assertEquals(menuSnapshot.getId(), menuItem.getSnapshot(1).getMenu().getId());
        assertEquals(menuSnapshot.getName(), menuItem.getSnapshot(1).getMenu().getName());
        assertTrue(menuItem.getSnapshot(1).getMenu().getMenuItems().isEmpty());
    }

    @Test
    void shouldUpdateMenuItemInfo() {
        // given
        final var menuItem = new MenuItem();

        // when
        menuItem.update(1L, "Salad", new Money(new BigDecimal("7.99")), 2L, "Healthy Menu");

        // then
        final var result = menuItem.getSnapshot(1);
        assertEquals(1L, result.getId());
        assertEquals("Salad", result.getName());
        assertEquals(new BigDecimal("7.99"), result.getPrice());
        assertNotNull(result.getMenu());
        assertEquals(Status.ACTIVE, result.getStatus());
    }
}

class MenuTest {

    @Test
    void shouldRestoreMenuFromSnapshot() {
        // given
        final var menuSnapshot = new MenuSnapshot(1L, "Main Menu", new HashSet<>());

        // when
        final var menu = Menu.restore(menuSnapshot, 0);

        // then
        final var result = menu.getSnapshot(1);
        assertEquals(menuSnapshot.getId(), result.getId());
        assertEquals(menuSnapshot.getName(), result.getName());
        assertTrue(result.getMenuItems().isEmpty());
    }

    @Test
    void shouldUpdateMenuInfo() {
        // given
        final var menu = new Menu();

        // when
        menu.updateInfo(1L, "Healthy Menu");

        // then
        final var result = menu.getSnapshot(1);
        assertEquals(1L, result.getId());
        assertEquals("Healthy Menu", result.getName());
        assertTrue(result.getMenuItems().isEmpty());
    }

    @Test
    void shouldUpdateMenuName() {
        // given
        final var menu = new Menu();

        // when
        menu.updateName("Vegetarian Menu");

        // then
        assertEquals("Vegetarian Menu", menu.getSnapshot(0).getName());
    }
}
