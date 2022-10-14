package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.MenuDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.MenuItemDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;

import java.util.List;
import java.util.Optional;

@Service
public class MenuItemServiceImp implements MenuItemService{

    @Autowired
    private MenuItemDAO menuItemDAO;

    @Autowired
    private MenuDAO menuDAO;

    @Override
    public List<MenuItem> findAll() {
        return menuItemDAO.findAll();
    }

    @Override
    public MenuItem findById(int theId) {
        Optional<MenuItem> result = menuItemDAO.findById(theId);

        MenuItem theMenuItem = null;

        if (result.isPresent()) {
            theMenuItem = result.get();
        }

        return theMenuItem;
    }

    @Override
    public void save(MenuItem theMenuItem) {
        menuItemDAO.save(theMenuItem);
    }

    @Override
    public void deleteById(int theId) {
        menuItemDAO.deleteById(theId);
    }

    @Override
    public List<MenuItem> findByMenu(String menuName) {
        Menu theMenu = menuDAO.findByName(menuName);
        return menuItemDAO.findByMenu(theMenu);
    }
}
