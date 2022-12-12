package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

@Repository
public interface MenuItemRepository extends JpaRepository<MenuItem, UUID> {
    List<MenuItem> findByMenuName(String menuName);
    Optional<MenuItem> findByName(String name);
}
