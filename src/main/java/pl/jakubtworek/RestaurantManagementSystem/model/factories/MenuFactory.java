package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

@Component
public class MenuFactory {
    public Menu createMenu(GetMenuDTO menuDTO){
        Menu menu = new Menu();
        menu.setId(0L);
        menu.setName(menuDTO.getName());
        menu.setMenuItems(null);
        return menu;
    }
}
