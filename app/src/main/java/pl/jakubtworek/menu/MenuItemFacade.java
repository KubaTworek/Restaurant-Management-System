package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that name doesn't exist";
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;

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
        return menuQueryRepository.findDtoByName(toSave.getMenu())
                .map(menu -> createAndSaveMenuItemWithExistingMenu(toSave, menu))
                .orElseGet(() -> createAndSaveMenuItemWithNewMenu(toSave));
    }

    List<MenuDto> findAll() {
        return new ArrayList<>(menuQueryRepository.findBy(MenuDto.class));
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

    private MenuItemDto createAndSaveMenuItemWithExistingMenu(MenuItemRequest toSave, MenuDto menuDto) {
        final var menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                toSave.getPrice(),
                menuDto.getId(),
                menuDto.getName()
        );
        return toDto(menuItemRepository.save(menuItem));
    }

    private MenuItemDto createAndSaveMenuItemWithNewMenu(MenuItemRequest toSave) {
        final var menuItem = new MenuItem();
        final var menu = menuItem.createNewMenu(toSave.getMenu());
        final var savedMenu = menuItemRepository.save(menu);
        menuItem.createWithMenu(
                toSave.getName(),
                toSave.getPrice(),
                savedMenu.getSnapshot().getId(),
                savedMenu.getSnapshot().getName()
        );
        return toDto(menuItemRepository.save(menuItem));
    }
}