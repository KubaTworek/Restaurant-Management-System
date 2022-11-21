package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
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

    public static List<MenuDTO> createMenuListDTO(){
        MenuDTO menu1 = spy(new MenuDTO(1L, "Drinks", createMenuItemListForDrinksDTO()));
        MenuDTO menu2 = spy(new MenuDTO(2L, "Food", createMenuItemListForFoodDTO()));
        return List.of(menu1, menu2);
    }

    public static Optional<Menu> createMenu(){
        return Optional.of(spy(new Menu(1L, "Drinks", createMenuItemListForDrinks())));
    }

    public static Optional<MenuDTO> createMenuDTO(){
        return Optional.of(spy(new MenuDTO(1L, "Drinks", createMenuItemListForDrinksDTO())));
    }

    private static List<MenuItem> createMenuItemListForDrinks() {
        MenuItem menuItem = new MenuItem(2L, "Coke", 1.99, null, List.of());
        return List.of(menuItem);
    }

    private static List<MenuItemDTO> createMenuItemListForDrinksDTO() {
        MenuItemDTO menuItem = new MenuItemDTO(2L, "Coke", 1.99, null, List.of());
        return List.of(menuItem);
    }

    private static List<MenuItem> createMenuItemListForFood() {
        MenuItem menuItem1 = new MenuItem(1L, "Chicken", 10.99, null, List.of());
        MenuItem menuItem2 = new MenuItem(3L, "Tiramisu", 5.99, null, List.of());
        return List.of(menuItem1, menuItem2);
    }

    private static List<MenuItemDTO> createMenuItemListForFoodDTO() {
        MenuItemDTO menuItem1 = new MenuItemDTO(1L, "Chicken", 10.99, null, List.of());
        MenuItemDTO menuItem2 = new MenuItemDTO(3L, "Tiramisu", 5.99, null, List.of());
        return List.of(menuItem1, menuItem2);
    }
}
