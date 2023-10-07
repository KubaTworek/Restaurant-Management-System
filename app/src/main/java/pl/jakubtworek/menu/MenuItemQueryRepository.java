package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;

import java.util.List;
import java.util.Optional;

public interface MenuItemQueryRepository {
    List<MenuItemDto> findDtoByMenuName(String menuName);

    Optional<MenuItemDto> findDtoById(Long id);
}
