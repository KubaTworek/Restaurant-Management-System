package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class MenuFacade {
    private final MenuItemFacade menuItemFacade;
    private final MenuRepository menuRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuFacade(final MenuItemFacade menuItemFacade, final MenuRepository menuRepository,
               final MenuQueryRepository menuQueryRepository) {
        this.menuItemFacade = menuItemFacade;
        this.menuRepository = menuRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    MenuDto save(MenuRequest toSave) {
        Menu menu = new Menu();
        menu.setName(toSave.getName());
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

    MenuDto toDto(Menu menu) {
        return MenuDto.builder()
                .withId(menu.getId())
                .withName(menu.getName())
                .withMenuItems(menu.getMenuItems().stream().map(menuItemFacade::toDto).collect(Collectors.toList()))
                .build();
    }
}