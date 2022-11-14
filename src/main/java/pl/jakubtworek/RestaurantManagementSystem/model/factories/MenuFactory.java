package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

@Component
public class MenuFactory {
    public Menu createMenu(MenuRequest menuDTO){
        return Menu.builder()
                        .id(0L)
                        .name(menuDTO.getName())
                        .menuItems(null)
                        .build();
    }
}
