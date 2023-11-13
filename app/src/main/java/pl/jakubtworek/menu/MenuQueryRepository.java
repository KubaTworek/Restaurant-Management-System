package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.Query;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.order.dto.Status;

import java.util.Optional;
import java.util.Set;

public interface MenuQueryRepository {
    Optional<MenuDto> findDtoByName(String theName);

    @Query("SELECT DISTINCT m FROM MenuSnapshot m LEFT JOIN FETCH m.menuItems mi WHERE mi.status = :status")
    Set<MenuDto> findDtoByMenuItems_Status(Status status);
}
