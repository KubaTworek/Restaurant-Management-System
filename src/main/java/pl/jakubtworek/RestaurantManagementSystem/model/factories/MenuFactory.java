package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

@Component
public class MenuFactory {
    public Menu createMenu(MenuRequest menuDTO){
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setName(menuDTO.getName());
        menu.setMenuItems(null);
        return menu;
    }
}
