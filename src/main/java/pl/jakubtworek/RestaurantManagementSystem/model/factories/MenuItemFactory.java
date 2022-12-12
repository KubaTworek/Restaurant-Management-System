package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class MenuItemFactory {
    public MenuItemDTO createMenuItem(
            MenuItemRequest menuItemRequest,
            MenuDTO menuDTO
    ){
        String name = menuItemRequest.getName();
        double price = menuItemRequest.getPrice();

        return MenuItemDTO.builder()
                .name(name)
                .price(price)
                .menu(menuDTO)
                .orders(null)
                .build();
    }

    public MenuItemDTO updateMenuItem(
            MenuItem oldMenuItem,
            MenuItemRequest newMenuitem
    ){
        UUID id = oldMenuItem.getId();
        String name = newMenuitem.getName();
        double price = newMenuitem.getPrice();
        MenuDTO menu = oldMenuItem.getMenu().convertEntityToDTO();
        List<OrderDTO> orders;
        if(oldMenuItem.getOrders() != null){
            orders = oldMenuItem.getOrders().stream().map(Order::convertEntityToDTO).collect(Collectors.toList());
        } else {
            orders = new ArrayList<>();
        }

        return MenuItemDTO.builder()
                .id(id)
                .name(name)
                .price(price)
                .menu(menu)
                .orders(orders)
                .build();
    }
}
