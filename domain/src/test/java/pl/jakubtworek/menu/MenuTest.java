package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
