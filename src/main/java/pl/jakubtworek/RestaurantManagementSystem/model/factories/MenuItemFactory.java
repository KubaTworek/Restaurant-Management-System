package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

@Component
public class MenuItemFactory {

    public MenuItem createMenuItem(MenuItemRequest menuItemDTO, Menu menu){
        MenuItem menuItem = new MenuItem();
        menuItem.setId(0L);
        menuItem.setName(menuItemDTO.getName());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setMenu(menu);
        menuItem.setOrders(null);
        return menuItem;
    }
}
