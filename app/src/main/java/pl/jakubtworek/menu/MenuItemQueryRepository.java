package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuItemQueryRepository extends JpaRepository<MenuItem, Long> {
    List<MenuItemDto> findDtoByMenuName(String menuName);

    Optional<MenuItemDto> findDtoById(Long id);
}
