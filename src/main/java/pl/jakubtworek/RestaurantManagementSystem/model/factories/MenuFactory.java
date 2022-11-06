package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.ArrayList;

public class MenuFactory {
    public Menu createMenu(GetMenuDTO menuDTO){
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setName(menuDTO.getName());
        menu.setMenuItems(new ArrayList<>());
        return menu;
    }
}
