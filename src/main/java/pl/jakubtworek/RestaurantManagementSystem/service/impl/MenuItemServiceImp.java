package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MenuItemServiceImp implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;
    private final MenuItemFactory menuItemFactory;

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItem> findById(Long theId) {
        return menuItemRepository.findById(theId);
    }

    @Override
    public MenuItem save(MenuItemRequest menuItemDTO) {
        Menu menu = menuRepository.findByName(menuItemDTO.getMenu()).get();
        MenuItem menuItem = menuItemFactory.createMenuItem(menuItemDTO, menu);
        return menuItemRepository.save(menuItem);
    }

    @Override
    public void deleteById(Long theId) {
        menuItemRepository.deleteById(theId);
    }

    @Override
    public List<MenuItem> findByMenu(String menuName) {
        if(menuRepository.findByName(menuName).isPresent()){
            return menuItemRepository.findByMenu(menuRepository.findByName(menuName).get());
        }
        return Collections.emptyList();
    }

    @Override
    public boolean checkIsMenuItemIsNull(Long id) {
        return findById(id).isEmpty();
    }
}