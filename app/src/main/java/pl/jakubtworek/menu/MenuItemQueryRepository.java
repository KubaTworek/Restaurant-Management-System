package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.List;
import java.util.Optional;

public interface MenuItemQueryRepository {
    List<MenuItemDto> findByMenuName(String menuName);

    Optional<MenuItemDto> findDtoById(Long id);

    Optional<SimpleMenuItem> findSimpleByName(String name);

}
