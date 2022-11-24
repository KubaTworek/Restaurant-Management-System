package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;

import static org.junit.jupiter.api.Assertions.*;

public class MenuItemFactoryTest {
    private MenuItemFactory menuItemFactory;

    @BeforeEach
    public void setUp() {
        menuItemFactory = new MenuItemFactory();
    }

    @Test
    public void shouldReturnMenuItem() {
        // given
        MenuDTO foodMenu = new MenuDTO(1L, "Food", null);
        MenuItemRequest menuItemDTO = new MenuItemRequest("Apple", 1.99, "Food");

        // when
        MenuItemDTO menuItem = menuItemFactory.createMenuItem(menuItemDTO, foodMenu);

        // then
        assertEquals("Apple", menuItem.getName());
        assertEquals(1.99, menuItem.getPrice());
        assertEquals("Food", menuItem.getMenu().getName());
        assertNull(menuItem.getOrders());
    }
}
