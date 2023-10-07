package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;

import java.util.Optional;
import java.util.Set;

public interface MenuQueryRepository {
    Optional<Menu> findByName(String theName);

    Optional<MenuDto> findDtoById(Long id);

    <T> Set<T> findBy(Class<T> type);
}
