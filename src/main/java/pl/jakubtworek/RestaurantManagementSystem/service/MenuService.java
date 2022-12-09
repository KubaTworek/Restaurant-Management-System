package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;

import java.util.List;
import java.util.Optional;

public interface MenuService {
    List<MenuDTO> findAll();
    Optional<MenuDTO> findById(Long theId);
    MenuDTO save(MenuRequest menuRequest);
    void deleteById(Long theId) throws MenuNotFoundException;
    Optional<MenuDTO> findByName(String theName);
}