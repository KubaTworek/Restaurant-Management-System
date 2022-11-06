package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

@Component
public class MenuItemFactory {
    private final MenuService menuService;

    public MenuItemFactory(MenuService menuService) {
        this.menuService = menuService;
    }

    public MenuItem createMenuItem(GetMenuItemDTO menuItemDTO){
        MenuItem menuItem = new MenuItem();
        menuItem.setId(0L);
        menuItem.setName(menuItemDTO.getName());
        menuItem.setPrice(menuItemDTO.getPrice());
        menuItem.setMenu(menuService.findByName(menuItemDTO.getMenu()).get());
        menuItem.setOrders(null);
        return menuItem;
    }
}
