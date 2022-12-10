package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;

@Component
public class MenuFactory {
    public MenuDTO createMenu(
            MenuRequest menuRequest
    ){
        String name = menuRequest.getName();

        return MenuDTO.builder()
                        .name(name)
                        .menuItems(null)
                        .build();
    }
}
