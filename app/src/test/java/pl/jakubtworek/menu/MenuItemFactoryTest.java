package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

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
                "Burger", 1599, "Food"
        );
        final var menuDto = MenuDto.create(
                1L, "Food", new ArrayList<>()
        );

        // when
        final var menuItem = menuItemFactory.createMenuItem(menuItemRequest, menuDto);

        // then
        final var snap = menuItem.getSnapshot();
        assertEquals("Burger", snap.getName());
        assertEquals(1599, snap.getPrice());
        assertEquals(1L, snap.getMenu().getId());
        assertEquals("Food", snap.getMenu().getName());
    }

    @Test
    void shouldCreateMenuItem_whenMenuNotExist() {
        // given
        final var menuItemRequest = new MenuItemRequest(
                "Burger", 1599, "Food"
        );
        final var menu = new MenuItem.Menu();
        menu.updateInfo(1L, "Food");

        when(menuItemRepository.save(any(MenuItem.Menu.class))).thenReturn(menu);

        // when
        final var menuItem = menuItemFactory.createMenuItemWithMenu(menuItemRequest);

        // then
        final var snap = menuItem.getSnapshot();
        assertEquals("Burger", snap.getName());
        assertEquals(1599, snap.getPrice());
        assertEquals("Food", snap.getMenu().getName());
    }
}
