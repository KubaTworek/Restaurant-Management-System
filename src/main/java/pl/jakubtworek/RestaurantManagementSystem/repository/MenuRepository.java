package pl.jakubtworek.RestaurantManagementSystem.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.*;

@Repository
public interface MenuRepository extends JpaRepository<Menu, UUID> {
    Optional<Menu> findByName(String theName);
}
