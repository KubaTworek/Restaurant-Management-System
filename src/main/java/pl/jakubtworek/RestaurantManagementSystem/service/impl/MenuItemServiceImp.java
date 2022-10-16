package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImp implements MenuItemService {

    private final MenuItemRepository menuItemRepository;
    private final MenuRepository menuRepository;

    @Autowired
    public MenuItemServiceImp(MenuItemRepository menuItemRepository, MenuRepository menuRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuRepository = menuRepository;
    }

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public Optional<MenuItem> findById(int theId) {
        return menuItemRepository.findById(theId);
    }

    @Override
    public MenuItem save(MenuItem theMenuItem) {
        return menuItemRepository.save(theMenuItem);
    }

    @Override
    public void deleteById(int theId) {
        menuItemRepository.deleteById(theId);
    }

    @Override
    public List<MenuItem> findByMenu(String menuName) {
        if(menuRepository.findByName(menuName).isPresent()){
            return menuItemRepository.findByMenu(menuRepository.findByName(menuName).get());
        }
        return Collections.emptyList();
    }
}