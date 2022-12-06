package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

import static org.mockito.Mockito.spy;

public class MenuUtils {
    public static List<Menu> createMenuList(){
        Menu menu1 = spy(new Menu(1L, "Drinks", createMenuItemListForDrinks()));
        Menu menu2 = spy(new Menu(2L, "Food", createMenuItemListForFood()));
        return List.of(menu1, menu2);
    }

    public static Menu createMenu(){
        return new Menu(1L, "Drinks", createMenuItemListForDrinks());
    }

    public static List<MenuItem> createMenuItemListForDrinks() {
        MenuItem menuItem = createCokeMenuItem();
        return List.of(menuItem);
    }

    public static List<MenuItem> createMenuItemListForFood() {
        MenuItem menuItem1 = createChickenMenuItem();
        MenuItem menuItem2 = createTiramisuMenuItem();
        return List.of(menuItem1, menuItem2);
    }

    public static MenuItem createChickenMenuItem() {
        return new MenuItem(1L, "Chicken", 10.99, null, List.of());
    }

    public static MenuItem createCokeMenuItem() {
        return new MenuItem(2L, "Coke", 1.99, null, List.of());
    }

    public static MenuItem createTiramisuMenuItem() {
        return new MenuItem(3L, "Tiramisu", 5.99, null, List.of());
    }

    public static MenuRequest createMenuRequest(){
        return new MenuRequest("Drinks");
    }

    public static MenuItemRequest createChickenMenuItemRequest() {
        return new MenuItemRequest("Chicken", 10.99, "Food");
    }
}
