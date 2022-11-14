package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class MenuItemFactoryTest {
    @Autowired
    private MenuItemFactory menuItemFactory;

    @BeforeEach
    public void setUp() {
        menuItemFactory = new MenuItemFactory();
    }

    @Test
    public void shouldReturnMenuItem() {
        // given
        Menu foodMenu = new Menu(1L, "Food", null);
        MenuItemRequest menuItemDTO = new MenuItemRequest(1L, "Apple", 1.99, "Food");

        // when
        MenuItem menuItem = menuItemFactory.createMenuItem(menuItemDTO, foodMenu);

        // then
        assertEquals("Apple", menuItem.getName());
        assertEquals(1.99, menuItem.getPrice());
        assertEquals("Food", menuItem.getMenu().getName());
        assertNull(menuItem.getOrders());
    }
}
