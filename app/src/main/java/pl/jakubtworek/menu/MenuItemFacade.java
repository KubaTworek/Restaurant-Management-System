package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that name doesn't exist";

    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;
    private final MenuItem menuItem;

    MenuItemFacade(final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository,
                   final MenuItem menuItem
    ) {
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
        this.menuItem = menuItem;
    }

    public MenuItemDto getByName(String name) {
        return menuItemQueryRepository.findDtoByName(name)
                .orElseThrow(() -> new IllegalStateException(MENU_ITEM_NOT_FOUND_ERROR));
    }

    MenuItemDto save(MenuItemRequest toSave) {
        return toDto(menuItemQueryRepository.findDtoByName(toSave.name())
                .map(menuItemDto -> getUpdatedMenuItem(toSave, menuItemDto))
                .orElseGet(() -> createMenuItem(toSave)));
    }

    void deleteById(Long id) {
        menuItem.deactivate(id);
    }

    List<MenuDto> findAll() {
        return new ArrayList<>(menuQueryRepository.findDtoByMenuItems_Status(Status.ACTIVE));
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findByMenuName(menuName);
    }

    private MenuItem getUpdatedMenuItem(MenuItemRequest toSave, MenuItemDto menuItemDto) {
        return menuQueryRepository.findDtoByName(toSave.menu())
                .map(menu -> menuItem.update(
                        menuItemDto.getId(),
                        toSave.name(),
                        toSave.price(),
                        menu.getId()
                ))
                .orElseGet(() -> menuItem.updateAndCreateMenu(
                        menuItemDto.getId(),
                        toSave.name(),
                        toSave.price(),
                        toSave.menu()
                ));
    }

    private MenuItem createMenuItem(MenuItemRequest toSave) {
        return menuQueryRepository.findDtoByName(toSave.menu())
                .map(menu -> menuItem.create(
                        toSave.name(),
                        toSave.price(),
                        menu.getId()
                ))
                .orElseGet(() -> menuItem.createWithMenu(
                        toSave.name(),
                        toSave.price(),
                        toSave.menu()
                ));
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        final var snap = menuItem.getSnapshot(1);
        return MenuItemDto.create(snap.getId(), snap.getName(), snap.getPrice(), snap.getStatus());
    }
}
