package pl.jakubtworek.RestaurantManagementSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;

import java.util.List;

public interface MenuItemDAO extends JpaRepository<MenuItem, Integer> {
    List<MenuItem> findByMenu(Menu menu);
}
