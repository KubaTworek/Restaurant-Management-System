package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemService {
    List<MenuItem> findAll();
    Optional<MenuItem> findById(int theId);
    MenuItem save(MenuItem theMenuItem);
    void deleteById(int theId);
    List<MenuItem> findByMenu(String menuName);
}