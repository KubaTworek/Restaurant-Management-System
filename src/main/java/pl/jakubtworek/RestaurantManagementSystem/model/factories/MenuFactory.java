package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;
import java.util.stream.Collectors;

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

    public MenuDTO updateMenu(
            MenuRequest newMenu,
            Menu oldMenu
    ){
        UUID id = oldMenu.getId();
        String name = newMenu.getName();
        List<MenuItemDTO> menuItems;
        if(oldMenu.getMenuItems() != null){
            menuItems = oldMenu.getMenuItems().stream().map(MenuItem::convertEntityToDTO).collect(Collectors.toList());
        } else {
            menuItems = new ArrayList<>();
        }

        return MenuDTO.builder()
                .id(id)
                .name(name)
                .menuItems(menuItems)
                .build();
    }
}
