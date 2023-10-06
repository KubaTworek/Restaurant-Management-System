package pl.jakubtworek.restaurant.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItem> findByMenuName(String menuName);
}