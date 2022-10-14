package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;

public interface MenuService {
    List<Menu> findAll();
    Menu findById(int theId);
    void save(Menu theMenu);
    void deleteById(int theId);

    Menu findByName(String theName);
}
