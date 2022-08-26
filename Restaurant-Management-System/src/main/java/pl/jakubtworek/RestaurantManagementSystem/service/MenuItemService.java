package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;

import java.util.List;

public interface MenuItemService {
    List<MenuItem> findAll();
    MenuItem findById(int theId);
    void save(MenuItem theMenuItem);
    void deleteById(int theId);

    List<MenuItem> findByMenu(String menuName);
}
