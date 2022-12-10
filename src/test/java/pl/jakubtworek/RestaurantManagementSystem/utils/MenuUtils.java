package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    public static class MenuAssertions<T extends Menu>{
        public static void checkAssertionsForMenu(Menu menu){
            assertEquals("Drinks", menu.getName());
            assertEquals("Coke", menu.getMenuItems().get(0).getName());
            assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
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

    public static class MenuDTOAssertions<T extends MenuDTO>{
        public static void checkAssertionsForMenu(MenuDTO menu){
            assertEquals("Drinks", menu.getName());
            assertEquals("Coke", menu.getMenuItems().get(0).getName());
            assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
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

    public static class MenuResponseAssertions<T extends MenuResponse>{
        public static void checkAssertionsForMenu(MenuResponse menu){
            assertEquals("Drinks", menu.getName());
            assertEquals("Coke", menu.getMenuItems().get(0).getName());
            assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
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

    public static class MenuItemAssertions<T extends MenuItem>{
        public static void checkAssertionsForMenuItem(MenuItem menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItem> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }
    }

    public static class MenuItemDTOAssertions<T extends MenuItemDTO>{
        public static void checkAssertionsForMenuItem(MenuItemDTO menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItemDTO> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }
    }

    public static class MenuItemResponseAssertions<T extends MenuItemResponse>{
        public static void checkAssertionsForMenuItem(MenuItemResponse menuItem) {
            assertEquals("Chicken", menuItem.getName());
            assertEquals(10.99, menuItem.getPrice());
        }

        public static void checkAssertionsForMenuItems(List<MenuItemResponse> menuItems) {
            assertEquals("Chicken", menuItems.get(0).getName());
            assertEquals(10.99, menuItems.get(0).getPrice());

            assertEquals("Tiramisu", menuItems.get(1).getName());
            assertEquals(5.99, menuItems.get(1).getPrice());
        }
    }
}
