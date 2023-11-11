package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that name doesn't exist";
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuItemFacade(final MenuItemRepository menuItemRepository,
                   final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository
    ) {
        this.menuItemRepository = menuItemRepository;
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    public MenuItemDto getByName(String name) {
        return menuItemQueryRepository.findDtoByName(name)
                .orElseThrow(() -> new IllegalStateException(MENU_ITEM_NOT_FOUND_ERROR));
    }

    public MenuItemDto getById(Long id) {
        return menuItemQueryRepository.findDtoById(id)
                .orElseThrow(() -> new IllegalStateException(MENU_ITEM_NOT_FOUND_ERROR));
    }

    MenuItemDto save(MenuItemRequest toSave) {
        return menuQueryRepository.findDtoByName(toSave.getMenu())
                .map(menu -> {
                    final var created = MenuItemFactory.createMenuItem(toSave, menu);
                    return toDto(menuItemRepository.save(created));
                })
                .orElseGet(() -> {
                    final var menu = MenuItemFactory.createMenu(toSave.getMenu());
                    final var createdMenu = toDto(menuItemRepository.save(menu));
                    final var created = MenuItemFactory.createMenuItem(toSave, createdMenu);
                    return toDto(menuItemRepository.save(created));
                });
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
        final var snap = menuItem.getSnapshot();
        return MenuItemDto.create(snap.getId(), snap.getName(), snap.getPrice());
    }

    private MenuDto toDto(MenuItem.Menu menu) {
        final var snap = menu.getSnapshot();
        return MenuDto.create(snap.getId(), snap.getName(), new ArrayList<>());
    }
}