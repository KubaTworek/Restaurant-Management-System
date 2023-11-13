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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

class MenuItemFactoryTest {
    @Mock
    private MenuItemRepository menuItemRepository;

    private MenuItemFactory menuItemFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        menuItemFactory = new MenuItemFactory(menuItemRepository);
    }

    @Test
    void shouldCreateMenuItem_whenMenuExist() {
        // given
        final var menuItemRequest = new MenuItemRequest(
                "Burger", new BigDecimal("15.99"), "Food"
        );
        final var menuDto = MenuDto.create(
                1L, "Food", new ArrayList<>()
        );

        // when
        final var menuItem = menuItemFactory.createMenuItem(menuItemRequest, menuDto);

        // then
        final var snap = menuItem.getSnapshot(1);
        assertEquals("Burger", snap.getName());
        assertEquals(15.99, snap.getPrice().doubleValue());
        assertEquals(1L, snap.getMenu().getId());
        assertEquals("Food", snap.getMenu().getName());
    }

    @Test
    void shouldCreateMenuItem_whenMenuNotExist() {
        // given
        final var menuItemRequest = new MenuItemRequest(
                "Burger", new BigDecimal("15.99"), "Food"
        );
        final var menu = new MenuItem.Menu();
        menu.updateInfo(1L, "Food");

        when(menuItemRepository.save(any(MenuItem.Menu.class))).thenReturn(menu);

        // when
        final var menuItem = menuItemFactory.createMenuItemAndMenu(menuItemRequest);

        // then
        final var snap = menuItem.getSnapshot(1);
        assertEquals("Burger", snap.getName());
        assertEquals(15.99, snap.getPrice().doubleValue());
        assertEquals("Food", snap.getMenu().getName());
    }
}
