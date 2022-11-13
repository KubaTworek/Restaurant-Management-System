package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.spy;

public class MenuUtils {
    public static List<Menu> createMenuList(){
        Menu menu1 = spy(new Menu(1L, "Drinks", createMenuItemListForDrinks()));
        Menu menu2 = spy(new Menu(2L, "Food", createMenuItemListForFood()));
        return List.of(menu1, menu2);
    }

    public static Optional<Menu> createMenu(){
        return Optional.of(spy(new Menu(1L, "Drinks", createMenuItemListForDrinks())));
    }

    private static List<MenuItem> createMenuItemListForDrinks() {
        MenuItem menuItem = new MenuItem(2L, "Coke", 1.99, null, List.of());
        return List.of(menuItem);
    }

    private static List<MenuItem> createMenuItemListForFood() {
        MenuItem menuItem1 = new MenuItem(1L, "Chicken", 10.99, null, List.of());
        MenuItem menuItem2 = new MenuItem(3L, "Tiramisu", 5.99, null, List.of());
        return List.of(menuItem1, menuItem2);
    }
}
