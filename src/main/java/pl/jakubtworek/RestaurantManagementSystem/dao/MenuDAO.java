package pl.jakubtworek.RestaurantManagementSystem.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;

public interface MenuDAO extends JpaRepository<Menu, Integer> {
    Menu findByName(String theName);
}
