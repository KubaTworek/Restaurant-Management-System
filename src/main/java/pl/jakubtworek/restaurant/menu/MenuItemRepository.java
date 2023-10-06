package pl.jakubtworek.restaurant.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MenuItemRepository extends JpaRepository<MenuItem, Long> {
    MenuItemDto saveAndReturnDto(MenuItem entity);
}