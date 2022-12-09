package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;

@Component
public class MenuItemFactory {
    public MenuItemDTO createMenuItem(
            MenuItemRequest menuItemRequest,
            MenuDTO menuDTO
    ){
        String name = menuItemRequest.getName();
        double price = menuItemRequest.getPrice();

        return MenuItemDTO.builder()
                .id(0L)
                .name(name)
                .price(price)
                .menu(menuDTO)
                .orders(null)
                .build();
    }
}
