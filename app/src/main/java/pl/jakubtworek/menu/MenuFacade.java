package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

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

    public Menu getByName(String name) {
        return menuQueryRepository.findByName(name)
                .orElseThrow(() -> new IllegalStateException("There are no menu in restaurant with that name: " + name));
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
                .withMenuItems(menu.getMenuItems().stream().map(this::toDto).collect(Collectors.toList()))
                .build();
    }

    MenuItemDto toDto(SimpleMenuItem menuItem) {
        return MenuItemDto.builder()
                .withId(menuItem.getId())
                .withName(menuItem.getName())
                .withPrice(menuItem.getPrice())
                .build();
    }
}