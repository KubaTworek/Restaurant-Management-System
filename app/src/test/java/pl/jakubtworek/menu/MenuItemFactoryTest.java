package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MenuItemFactoryTest {
    @Mock
    private MenuItemRepository menuItemRepository;

    private MenuItemFactory menuItemFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItemFactory = new MenuItemFactory(menuItemRepository);
    }

    @Test
    void shouldCreateMenuItem_whenMenuExist() {
        // given
        final var request = new MenuItemRequest("Burger", BigDecimal.valueOf(10.99), "Food");
        final var menu = MenuDto.create(1L, "Food", new ArrayList<>());

        // when
        final var menuItem = menuItemFactory.createMenuItem(request, menu);

        // then
        assertMenuItemCreated(menuItem);

    }

    @Test
    void shouldCreateMenuItemAndMenu_whenMenuNotExist() {
        // given
        final var request = new MenuItemRequest("Burger", BigDecimal.valueOf(10.99), "Food");
        final var menu = new Menu();
        menu.updateInfo(1L, "Food");

        when(menuItemRepository.save(any(Menu.class))).thenReturn(menu);

        // when
        final var menuItem = menuItemFactory.createMenuItemAndMenu(request);

        // then
        assertMenuItemCreated(menuItem);
    }

    @Test
    void shouldUpdateMenuItem_whenMenuExist() {
        // given
        final var request = new MenuItemRequest("Burger", BigDecimal.valueOf(10.99), "Food");
        final var menu = MenuDto.create(1L, "Food", null);

        // when
        final var menuItem = menuItemFactory.updateMenuItem(1L, request, menu);

        // then
        assertMenuItemUpdated(menuItem);
    }

    @Test
    void shouldUpdateMenuItemAndCreateMenu_whenMenuNotExist() {
        // given
        final var request = new MenuItemRequest("Burger", BigDecimal.valueOf(10.99), "Food");
        final var menu = new Menu();
        menu.updateInfo(1L, "Food");

        when(menuItemRepository.save(any(Menu.class))).thenReturn(menu);

        // when
        final var menuItem = menuItemFactory.updateMenuItemAndCreateMenu(1L, request);

        // then
        assertMenuItemUpdated(menuItem);
    }

    private void assertMenuItemCreated(MenuItem menuItem) {
        final var result = menuItem.getSnapshot(1);
        assertNull(result.getId());
        assertEquals("Burger", result.getName());
        assertEquals(10.99, result.getPrice().doubleValue());
        assertEquals(1L, result.getMenu().getId());
        assertEquals("Food", result.getMenu().getName());
    }

    private void assertMenuItemUpdated(MenuItem menuItem) {
        final var result = menuItem.getSnapshot(1);
        assertNotNull(result.getId());
        assertEquals("Burger", result.getName());
        assertEquals(10.99, result.getPrice().doubleValue());
        assertEquals(1L, result.getMenu().getId());
        assertEquals("Food", result.getMenu().getName());
    }
}
