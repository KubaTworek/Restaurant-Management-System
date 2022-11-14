package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

@Component
public class MenuItemFactory {
    public MenuItem createMenuItem(MenuItemRequest menuItemDTO, Menu menu){
        return MenuItem.builder()
                .id(0L)
                .name(menuItemDTO.getName())
                .price(menuItemDTO.getPrice())
                .menu(menu)
                .orders(null)
                .build();
    }
}
