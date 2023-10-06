package pl.jakubtworek.restaurant.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
interface MenuRepository extends JpaRepository<Menu, Long> {
    MenuDto saveAndReturnDto(Menu entity);
}