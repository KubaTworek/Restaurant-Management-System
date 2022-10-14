package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.MenuDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;

import java.util.List;
import java.util.Optional;

@Service
public class MenuServiceImpl implements MenuService{

    private final MenuDAO menuDAO;

    public MenuServiceImpl(MenuDAO menuDAO) {
        this.menuDAO = menuDAO;
    }

    @Override
    public List<Menu> findAll() {
        return menuDAO.findAll();
    }

    @Override
    public Menu findById(int theId) {
        Optional<Menu> result = menuDAO.findById(theId);

        Menu theMenu = null;

        if (result.isPresent()) {
            theMenu = result.get();
        }

        return theMenu;
    }

    @Override
    public void save(Menu theMenu) {
        menuDAO.save(theMenu);
    }

    @Override
    public void deleteById(int theId) {
        menuDAO.deleteById(theId);
    }

    @Override
    public Menu findByName(String menuName) {
        return menuDAO.findByName(menuName);
    }
}
