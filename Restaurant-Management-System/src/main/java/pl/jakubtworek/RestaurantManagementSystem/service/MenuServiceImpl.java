package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.MenuDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;

import java.util.List;

@Service
public class MenuServiceImpl implements MenuItemService{

    @Autowired
    private MenuDAO menuDAO;

    @Override
    public List<MenuItem> findAll() {
        return null;
    }

    @Override
    public MenuItem findById(int theId) {
        return null;
    }

    @Override
    public void save(MenuItem theMenuItem) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public List<MenuItem> findByMenu(String menuName) {
        return null;
    }
}
