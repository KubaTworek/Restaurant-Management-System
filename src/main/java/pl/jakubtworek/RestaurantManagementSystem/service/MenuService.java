package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<Menu> findAll();
    Optional<Menu> findById(Long theId);
    Menu save(MenuDTO theMenu);
    void deleteById(Long theId);
    Optional<Menu> findByName(String theName);
    boolean checkIsMenuIsNull(Long id);
}