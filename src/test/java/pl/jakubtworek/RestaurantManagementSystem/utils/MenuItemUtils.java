package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.spy;

public class MenuItemUtils {

    public static Optional<MenuItem> createChickenMenuItem() {
        return Optional.of(new MenuItem(1L, "Chicken", 10.99, null, List.of()));
    }

    public static Optional<MenuItemDTO> createChickenMenuItemDTO() {
        return Optional.of(new MenuItemDTO(1L, "Chicken", 10.99, null, List.of()));
    }

    public static Optional<MenuItem> createCokeMenuItem() {
        return Optional.of(new MenuItem(2L, "Coke", 1.99, null, List.of()));
    }

    public static Optional<MenuItemDTO> createCokeMenuItemDTO() {
        return Optional.of(new MenuItemDTO(2L, "Coke", 1.99, null, List.of()));
    }

    public static Optional<MenuItem> createTiramisuMenuItem() {
        return Optional.of(new MenuItem(3L, "Tiramisu", 5.99, null, List.of()));
    }

    public static Optional<MenuItemDTO> createTiramisuMenuItemDTO() {
        return Optional.of(new MenuItemDTO(3L, "Tiramisu", 5.99, null, List.of()));
    }
}
