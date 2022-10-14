package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImp implements MenuItemService{

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Override
    public List<MenuItem> findAll() {
        return menuItemRepository.findAll();
    }

    @Override
    public MenuItem findById(int theId) {
        Optional<MenuItem> result = menuItemRepository.findById(theId);

        MenuItem theMenuItem = null;

        if (result.isPresent()) {
            theMenuItem = result.get();
        }

        return theMenuItem;
    }

    @Override
    public void save(MenuItem theMenuItem) {
        menuItemRepository.save(theMenuItem);
    }

    @Override
    public void deleteById(int theId) {
        menuItemRepository.deleteById(theId);
    }

    @Override
    public List<MenuItem> findByMenu(String menuName) {
        Menu theMenu = menuRepository.findByName(menuName);
        return menuItemRepository.findByMenu(theMenu);
    }
}
