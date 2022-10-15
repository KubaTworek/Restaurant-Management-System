package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> findAll();
    Optional<Menu> findById(int theId);
    Menu save(Menu theMenu);
    void deleteById(int theId);
    Optional<Menu> findByName(String theName);
}