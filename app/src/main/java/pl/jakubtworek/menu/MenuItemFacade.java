package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that name doesn't exist";
    private static final String MENU_NOT_FOUND_ERROR = "Menu with that name doesn't exist";

    MenuItemFacade(final MenuItemRepository menuItemRepository, final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    public SimpleMenuItem getByName(String name) {
        return menuItemQueryRepository.findSimpleByName(name)
                .orElseThrow(() -> new IllegalStateException(MENU_ITEM_NOT_FOUND_ERROR));
    }

    MenuItemDto save(MenuItemRequest toSave) {
        final var menuDto = menuQueryRepository.findDtoByName(toSave.getMenu())
                .orElseThrow(() -> new IllegalStateException(MENU_NOT_FOUND_ERROR));

        final var menu = new Menu();
        menu.updateInfo(
                menuDto.getId(),
                menuDto.getName()
        );

        final var menuItem = new MenuItem();
        menuItem.updateInfo(
                toSave.getName(),
                toSave.getPrice(),
                menu
        );

        return toDto(menuItemRepository.save(menuItem));
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findByMenuName(menuName);
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        var snap = menuItem.getSnapshot();
        return MenuItemDto.create(snap.getId(), snap.getName(), snap.getPrice());
    }
}