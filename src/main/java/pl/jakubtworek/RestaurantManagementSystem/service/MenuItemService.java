package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;

import java.util.*;

public interface MenuItemService {
    MenuItemDTO save(MenuItemRequest theMenuItem) throws MenuNotFoundException;
    MenuItemDTO update(MenuItemRequest theMenuItem, UUID menuItemId) throws MenuNotFoundException;
    void deleteById(UUID theId) throws MenuItemNotFoundException;
    Optional<MenuItemDTO> findById(UUID theId);
    List<MenuItemDTO> findByMenu(String menuName) throws MenuNotFoundException;
}