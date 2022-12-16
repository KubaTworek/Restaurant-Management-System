package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuItemFactoryTest {
    private MenuItemFactory menuItemFactory;

    @BeforeEach
    void setUp() {
        menuItemFactory = new MenuItemFactory();
    }

    @Test
    void shouldReturnCreatedMenuItem() {
        // given
        MenuDTO foodMenu = createMenu().convertEntityToDTO();
        MenuItemRequest menuItemDTO = createChickenMenuItemRequest();

        // when
        MenuItemDTO menuItem = menuItemFactory.createMenuItem(menuItemDTO, foodMenu);

        // then
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
        assertEquals("Food", menuItem.getMenu().getName());
        assertNull(menuItem.getOrders());
    }

    @Test
    void shouldReturnUpdatedMenuItem() {
        // given
        Menu foodMenu = createMenu();
        MenuItemRequest newMenuItem = new MenuItemRequest("Chicken Wings", 9.99, "Food");
        MenuItem oldMenuItem = new MenuItem(UUID.randomUUID(), "Chicken", 10.99, foodMenu, List.of());

        // when
        MenuItemDTO menuItemCreated = menuItemFactory.updateMenuItem(oldMenuItem, newMenuItem);

        // then
        assertEquals("Chicken Wings", menuItemCreated.getName());
        assertEquals(9.99, menuItemCreated.getPrice());
        assertEquals("Food", menuItemCreated.getMenu().getName());
    }
}