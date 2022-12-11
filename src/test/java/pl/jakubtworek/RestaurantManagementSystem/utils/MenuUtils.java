package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;

public class MenuUtils {
    public static List<Menu> createMenuList(){
        Menu menu1 = spy(new Menu(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002"), "Drinks", createMenuItemListForDrinks()));
        Menu menu2 = spy(new Menu(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002"), "Food", createMenuItemListForFood()));
        return List.of(menu1, menu2);
    }

    public static Menu createMenu(){
        return new Menu(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002"), "Food", createMenuItemListForFood());
    }

    public static ArrayList<MenuItem> createMenuItemListForDrinks() {
        MenuItem menuItem = createCokeMenuItem();
        return new ArrayList<>(List.of(menuItem));
    }

    public static ArrayList<MenuItem> createMenuItemListForFood() {
        MenuItem menuItem1 = createChickenMenuItem();
        MenuItem menuItem2 = createTiramisuMenuItem();
        return new ArrayList<>(List.of(menuItem1, menuItem2));
    }

    public static MenuItem createChickenMenuItem() {
        return new MenuItem(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002"), "Chicken", 10.99, null, List.of());
    }

    public static MenuItem createCokeMenuItem() {
        return new MenuItem(UUID.fromString("4b14e962-7847-11ed-a1eb-0242ac120002"), "Coke", 1.99, null, List.of());
    }

    public static MenuItem createTiramisuMenuItem() {
        return new MenuItem(UUID.fromString("4ebbb050-7847-11ed-a1eb-0242ac120002"), "Tiramisu", 5.99, null, List.of());
    }

    public static MenuRequest createMenuRequest(){
        return new MenuRequest("Food");
    }

    public static MenuItemRequest createChickenMenuItemRequest() {
        return new MenuItemRequest("Chicken", 10.99, "Food");
    }

    public static class MenuAssertions {
        public static void checkAssertionsForMenu(Menu menu){
            assertEquals("Food", menu.getName());
            assertEquals("Chicken", menu.getMenuItems().get(0).getName());
            assertEquals(10.99, menu.getMenuItems().get(0).getPrice());

            assertEquals("Tiramisu", menu.getMenuItems().get(1).getName());
            assertEquals(5.99, menu.getMenuItems().get(1).getPrice());
        }

        public static void checkAssertionsForMenus(List<Menu> menus){
            assertEquals("Drinks", menus.get(0).getName());
            assertEquals("Coke", menus.get(0).getMenuItems().get(0).getName());
            assertEquals(1.99, menus.get(0).getMenuItems().get(0).getPrice());

            assertEquals("Food", menus.get(1).getName());
            assertEquals("Chicken", menus.get(1).getMenuItems().get(0).getName());
            assertEquals(10.99, menus.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Tiramisu", menus.get(1).getMenuItems().get(1).getName());
            assertEquals(5.99, menus.get(1).getMenuItems().get(1).getPrice());
        }
    }

    public static class MenuDTOAssertions {
        public static void checkAssertionsForMenu(MenuDTO menu){
            assertEquals("Food", menu.getName());
            assertEquals("Chicken", menu.getMenuItems().get(0).getName());
            assertEquals(10.99, menu.getMenuItems().get(0).getPrice());

            assertEquals("Tiramisu", menu.getMenuItems().get(1).getName());
            assertEquals(5.99, menu.getMenuItems().get(1).getPrice());
        }

        public static void checkAssertionsForMenus(List<MenuDTO> menus){
            assertEquals("Drinks", menus.get(0).getName());
            assertEquals("Coke", menus.get(0).getMenuItems().get(0).getName());
            assertEquals(1.99, menus.get(0).getMenuItems().get(0).getPrice());

            assertEquals("Food", menus.get(1).getName());
            assertEquals("Chicken", menus.get(1).getMenuItems().get(0).getName());
            assertEquals(10.99, menus.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Tiramisu", menus.get(1).getMenuItems().get(1).getName());
            assertEquals(5.99, menus.get(1).getMenuItems().get(1).getPrice());
        }
    }

    public static class MenuResponseAssertions {
        public static void checkAssertionsForMenu(MenuResponse menu){
            assertEquals("Food", menu.getName());
            assertEquals("Chicken", menu.getMenuItems().get(0).getName());
            assertEquals(10.99, menu.getMenuItems().get(0).getPrice());

            assertEquals("Tiramisu", menu.getMenuItems().get(1).getName());
            assertEquals(5.99, menu.getMenuItems().get(1).getPrice());
        }

        public static void checkAssertionsForMenus(List<MenuResponse> menus){
            assertEquals("Drinks", menus.get(0).getName());
            assertEquals("Coke", menus.get(0).getMenuItems().get(0).getName());
            assertEquals(1.99, menus.get(0).getMenuItems().get(0).getPrice());

            assertEquals("Food", menus.get(1).getName());
            assertEquals("Chicken", menus.get(1).getMenuItems().get(0).getName());
            assertEquals(10.99, menus.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Tiramisu", menus.get(1).getMenuItems().get(1).getName());
            assertEquals(5.99, menus.get(1).getMenuItems().get(1).getPrice());
        }
    }

    public static class MenuItemAssertions {
        public static void checkAssertionsForMenuItem(MenuItem menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItemsInMenu(List<MenuItem> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItem> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Coke", menuItems.get(1).getName());
            assertEquals(1.99, menuItems.get(1).getPrice());

            assertEquals("Tiramisu", menuItems.get(2).getName());
            assertEquals(5.99, menuItems.get(2).getPrice());
        }
    }

    public static class MenuItemDTOAssertions {
        public static void checkAssertionsForMenuItem(MenuItemDTO menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItemsInMenu(List<MenuItemDTO> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItemDTO> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Coke", menuItems.get(1).getName());
            assertEquals(1.99, menuItems.get(1).getPrice());

            assertEquals("Tiramisu", menuItems.get(2).getName());
            assertEquals(5.99, menuItems.get(2).getPrice());
        }
    }

    public static class MenuItemResponseAssertions {
        public static void checkAssertionsForMenuItem(MenuItemResponse menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItemsInMenu(List<MenuItemResponse> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItemResponse> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Coke", menuItems.get(1).getName());
            assertEquals(1.99, menuItems.get(1).getPrice());

            assertEquals("Tiramisu", menuItems.get(2).getName());
            assertEquals(5.99, menuItems.get(2).getPrice());
        }
    }
}
