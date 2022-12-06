package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<MenuItemDTO> findAll();
    Optional<MenuItemDTO> findById(Long theId);
    MenuItemDTO save(MenuItemRequest theMenuItem, MenuDTO menuDTO);
    void deleteById(Long theId);
    List<MenuItemDTO> findByMenu(Menu theMenu);
    Optional<MenuItemDTO> findByName(String name);
}