package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.common.vo.Status;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuItemTest {

    @Mock
    private MenuItemRepository repository;
    @Mock
    private DomainEventPublisher publisher;

    private final MenuItem menuItem = MenuItem.restore(new MenuItemSnapshot(1L, "Burger", BigDecimal.valueOf(10.99), new MenuSnapshot(1L, "Food", new HashSet<>()), Status.ACTIVE), 1);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItem.setDependencies(
                publisher,
                repository
        );

        when(repository.save(menuItem)).thenReturn(menuItem);
        when(repository.save(any(Menu.class))).thenReturn(Menu.restore(menuItem.getSnapshot(1).getMenu(), 1));
        when(repository.findById(1L)).thenReturn(Optional.of(menuItem));
        when(repository.findMenuById(1L)).thenReturn(Optional.of(Menu.restore(menuItem.getSnapshot(1).getMenu(), 1)));
    }

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
    void shouldUpdateMenuItemWithExistingMenu() {
        // given
        menuItem.deactivate(1L);

        // when
        final var updatedMenuItem = menuItem.update(1L, "Cheeseburger", BigDecimal.valueOf(11.99), 1L);

        // then
        assertMenuItem(updatedMenuItem);
        verify(repository, times(2)).save(updatedMenuItem);
    }

    @Test
    void shouldUpdateMenuItemAndCreateMenu() {
        // given
        menuItem.deactivate(1L);

        // when
        final var updatedMenuItem = menuItem.updateAndCreateMenu(1L, "Cheeseburger", BigDecimal.valueOf(11.99), "Food");

        // then
        assertMenuItem(updatedMenuItem);
        verify(repository, times(2)).save(updatedMenuItem);
    }

    @Test
    void shouldCreateMenuItem() {
        // when
        final var createdMenuItem = menuItem.create("Cheeseburger", BigDecimal.valueOf(11.99), 1L);

        // then
        assertMenuItem(createdMenuItem);
        verify(repository, times(1)).save(createdMenuItem);
    }

    @Test
    void shouldCreateMenuItemAndMenu() {
        // when
        final var createdMenuItem = menuItem.createWithMenu("Cheeseburger", BigDecimal.valueOf(11.99), "Food");

        // then
        assertMenuItem(createdMenuItem);
        verify(repository, times(1)).save(createdMenuItem);
    }

    @Test
    void shouldDeactivateMenuItem() {
        // when
        menuItem.deactivate(1L);

        // then
        assertEquals(Status.INACTIVE, menuItem.getSnapshot(1).getStatus());
        verify(repository, times(1)).save(menuItem);
    }

    @Test
    void shouldValidateStatus() {
        // when & then
        assertThrows(IllegalStateException.class,
                () -> menuItem.updateAndCreateMenu(1L, "Cheeseburger", BigDecimal.valueOf(11.99), "Food"));
        assertThrows(IllegalStateException.class,
                () -> menuItem.update(1L, "Cheeseburger", BigDecimal.valueOf(11.99), 1L));
        menuItem.deactivate(1L);
        assertThrows(IllegalStateException.class, () -> menuItem.deactivate(1L));
    }

    private void assertMenuItem(MenuItem menuItem) {
        final var result = menuItem.getSnapshot(1);
        assertEquals("Cheeseburger", result.getName());
        assertEquals(BigDecimal.valueOf(11.99), result.getPrice());
        assertEquals(1L, result.getMenu().getId());
        assertEquals("Food", result.getMenu().getName());
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
