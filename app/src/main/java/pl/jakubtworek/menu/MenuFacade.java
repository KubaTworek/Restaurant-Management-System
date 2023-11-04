package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItemSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuFacade {
    private final MenuRepository menuRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuFacade(final MenuRepository menuRepository, final MenuQueryRepository menuQueryRepository) {
        this.menuRepository = menuRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    MenuDto save(MenuRequest toSave) {
        final var menu = new Menu();
        menu.updateName(
                toSave.getName()
        );
        return toDto(menuRepository.save(menu));
    }

    void deleteById(Long id) {
        menuRepository.deleteById(id);
    }

    List<MenuDto> findAll() {
        return new ArrayList<>(menuQueryRepository.findBy(MenuDto.class));
    }

    Optional<MenuDto> findById(Long id) {
        return menuQueryRepository.findDtoById(id);
    }

    private MenuDto toDto(Menu menu) {
        var snap = menu.getSnapshot();
        return MenuDto.create(snap.getId(), snap.getName(), snap.getMenuItems().stream().map(this::toDto).collect(Collectors.toList()));
    }

    MenuItemDto toDto(SimpleMenuItemSnapshot menuItem) {
        return MenuItemDto.create(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
    }
}