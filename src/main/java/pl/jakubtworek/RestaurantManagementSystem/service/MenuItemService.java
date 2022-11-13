package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<MenuItem> findAll();
    Optional<MenuItem> findById(Long theId);
    MenuItem save(MenuItemRequest theMenuItem);
    void deleteById(Long theId);
    List<MenuItem> findByMenu(String menuName);
    boolean checkIsMenuItemIsNull(Long id);

}