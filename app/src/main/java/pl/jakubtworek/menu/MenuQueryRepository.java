package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.order.dto.Status;

import java.util.Optional;
import java.util.Set;

public interface MenuQueryRepository {
    Optional<MenuDto> findDtoByName(String theName);

    Set<MenuDto> findDtoByMenuItems_Status(Status status);
}
