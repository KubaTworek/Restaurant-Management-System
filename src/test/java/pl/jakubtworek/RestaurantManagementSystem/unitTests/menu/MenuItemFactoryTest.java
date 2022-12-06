package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

public class MenuItemFactoryTest {
    private MenuItemFactory menuItemFactory;

    @BeforeEach
    public void setUp() {
        menuItemFactory = new MenuItemFactory();
    }

    @Test
    public void shouldReturnMenuItem() {
        // given
        MenuDTO foodMenu = createMenu().convertEntityToDTO();
        MenuItemRequest menuItemDTO = createChickenMenuItemRequest();

        // when
        MenuItemDTO menuItem = menuItemFactory.createMenuItem(menuItemDTO, foodMenu);

        // then
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
        assertEquals("Drinks", menuItem.getMenu().getName());
        assertNull(menuItem.getOrders());
    }
}
