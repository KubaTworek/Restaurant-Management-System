package pl.jakubtworek.menu;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.jakubtworek.menu.dto.MenuDto;

import java.util.List;
import java.util.Optional;

@Repository
public interface MenuQueryRepository extends JpaRepository<Menu, Long> {
    Optional<Menu> findByName(String theName);

    List<MenuDto> findAllDtoBy();

    Optional<MenuDto> findDtoById(Long id);
}
