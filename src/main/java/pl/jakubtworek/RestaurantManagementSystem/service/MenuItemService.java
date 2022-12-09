package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;

import java.util.*;

public interface MenuItemService {
    MenuItemDTO save(MenuItemRequest theMenuItem) throws MenuNotFoundException;
    void deleteById(Long theId) throws MenuItemNotFoundException;
    Optional<MenuItemDTO> findById(Long theId);
    List<MenuItemDTO> findByMenu(String menuName) throws MenuNotFoundException;
}