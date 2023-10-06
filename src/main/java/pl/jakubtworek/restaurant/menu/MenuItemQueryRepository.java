package pl.jakubtworek.restaurant.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface MenuItemQueryRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItemDto> findDtoByMenuName(String menuName);

    Optional<MenuItemDto> findDtoById(Long id);
}
