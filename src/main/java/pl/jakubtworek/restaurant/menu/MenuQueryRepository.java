package pl.jakubtworek.restaurant.menu;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

interface MenuQueryRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByName(String theName);

    List<MenuDto> findAllDtoBy();

    Optional<MenuDto> findDtoById(Long id);
}
