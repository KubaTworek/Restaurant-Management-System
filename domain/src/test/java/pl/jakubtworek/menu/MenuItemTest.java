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
        assertMenuItemFromSnapshot(snapshot, menuItem);
    }

    @Test
    void shouldRestoreMenuItemWithMenuFromSnapshot() {
        // given
        final var snapshot = new MenuItemSnapshot(1L, "Burger", new BigDecimal("10.99"), null, Status.ACTIVE);

        // when
        final var menuItem = MenuItem.restore(snapshot, 0);

        // then
        assertMenuItemFromSnapshot(snapshot, menuItem);
    }

    @Test
    void shouldUpdateMenuItemInfoWithMenuIdAndMenuName() {
        // given
        final var menuItem = new MenuItem();

        // when
        menuItem.update(1L, "Burger", new Money(BigDecimal.valueOf(7.99)), 2L, "Food");

        // then
        assertMenuItemWithMenuIdAndName(menuItem, BigDecimal.valueOf(7.99));
        assertEquals(1L, menuItem.getSnapshot(1).getId());
    }

    @Test
    void shouldUpdateMenuItemWithMenuObject() {
        // given
        final var menuItem = new MenuItem();
        final var menu = new Menu();

        // when
        menuItem.update(1L, "Burger", new Money(BigDecimal.valueOf(7.99)), menu);

        // then
        assertMenuItemWithMenuObject(menuItem, BigDecimal.valueOf(7.99));
        assertEquals(1L, menuItem.getSnapshot(1).getId());
    }

    @Test
    void shouldCreateMenuItemWithMenuIdAndMenuName() {
        // given
        final var menuItem = new MenuItem();

        // when
        menuItem.createWithMenu("Burger", new Money(BigDecimal.valueOf(7.99)), 2L, "Food");

        // then
        assertMenuItemWithMenuIdAndName(menuItem, BigDecimal.valueOf(7.99));
        assertNull(menuItem.getSnapshot(1).getId());
    }

    @Test
    void shouldCreateMenuItemWithMenuObject() {
        // given
        final var menuItem = new MenuItem();
        final var menu = new Menu();

        // when
        menuItem.createWithMenu("Burger", new Money(BigDecimal.valueOf(7.99)), menu);

        // then
        assertMenuItemWithMenuObject(menuItem, BigDecimal.valueOf(7.99));
        assertNull(menuItem.getSnapshot(1).getId());
    }

    private void assertMenuItemWithMenuIdAndName(MenuItem menuItem, BigDecimal expectedPrice) {
        final var result = menuItem.getSnapshot(1);
        assertEquals("Burger", result.getName());
        assertEquals(expectedPrice, result.getPrice());
        assertNotNull(result.getMenu());
        assertEquals(Status.ACTIVE, result.getStatus());

        final var menuSnap = result.getMenu();
        assertEquals(2, menuSnap.getId());
        assertEquals("Food", menuSnap.getName());
        assertTrue(menuSnap.getMenuItems().isEmpty());
    }

    private void assertMenuItemWithMenuObject(MenuItem menuItem, BigDecimal expectedPrice) {
        final var result = menuItem.getSnapshot(1);
        assertEquals("Burger", result.getName());
        assertEquals(expectedPrice, result.getPrice());
        assertEquals(Status.ACTIVE, result.getStatus());
    }

    private void assertMenuItemFromSnapshot(MenuItemSnapshot snapshot, MenuItem menuItem) {
        final var result = menuItem.getSnapshot(1);
        assertEquals(snapshot.getId(), result.getId());
        assertEquals(snapshot.getName(), result.getName());
        assertEquals(snapshot.getPrice(), result.getPrice());
        assertNull(result.getMenu());
        assertEquals(snapshot.getStatus(), result.getStatus());
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
        menu.updateInfo(1L, "Food");

        // then
        final var result = menu.getSnapshot(1);
        assertEquals(1L, result.getId());
        assertEquals("Food", result.getName());
        assertTrue(result.getMenuItems().isEmpty());
    }

    @Test
    void shouldUpdateMenuName() {
        // given
        final var menu = new Menu();

        // when
        menu.updateName("Food");

        // then
        assertEquals("Food", menu.getSnapshot(0).getName());
    }
}
