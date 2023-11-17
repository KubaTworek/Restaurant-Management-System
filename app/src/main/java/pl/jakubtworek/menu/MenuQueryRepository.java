package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;

import java.util.Optional;
import java.util.Set;

interface MenuQueryRepository {
    Optional<MenuDto> findDtoByName(String name);

    Set<MenuDto> findDtoByMenuItems_Status(Status status);
}
