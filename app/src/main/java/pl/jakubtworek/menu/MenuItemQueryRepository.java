package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;

import java.util.List;
import java.util.Optional;

interface MenuItemQueryRepository {
    List<MenuItemDto> findByMenuName(String menuName);

    Optional<MenuItemDto> findDtoById(Long id);

    Optional<MenuItemDto> findDtoByName(String theName);
}
