package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.common.vo.Status;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private static final String MENU_ITEM_NOT_FOUND_ERROR = "Menu item with that name doesn't exist";
    private final MenuItemFactory menuItemFactory;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuItemFacade(final MenuItemFactory menuItemFactory,
                   final MenuItemRepository menuItemRepository,
                   final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository
    ) {
        this.menuItemFactory = menuItemFactory;
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
        return menuItemQueryRepository.findDtoByName(toSave.name())
                .map(menuItem -> {
                    if (menuItem.getStatus() == Status.ACTIVE) {
                        throw new IllegalStateException("Menu item with that name: " + menuItem.getName() + " is already exist!");
                    }
                    return menuQueryRepository.findDtoByName(toSave.menu())
                            .map(menu -> {
                                final var updated = menuItemFactory.updateMenuItem(menuItem.getId(), toSave, menu);
                                return toDto(menuItemRepository.save(updated));
                            })
                            .orElseGet(() -> {
                                final var updated = menuItemFactory.updateMenuItemAndCreateMenu(menuItem.getId(), toSave);
                                return toDto(menuItemRepository.save(updated));
                            });
                })
                .orElseGet(() -> menuQueryRepository.findDtoByName(toSave.menu())
                        .map(menu -> {
                            final var created = menuItemFactory.createMenuItem(toSave, menu);
                            return toDto(menuItemRepository.save(created));
                        })
                        .orElseGet(() -> {
                            final var created = menuItemFactory.createMenuItemAndMenu(toSave);
                            return toDto(menuItemRepository.save(created));
                        }));
    }

    List<MenuDto> findAll() {
        return new ArrayList<>(menuQueryRepository.findDtoByMenuItems_Status(Status.ACTIVE));
    }

    int deleteById(Long id) {
        return menuItemRepository.deactivateMenuItem(id);
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findByMenuName(menuName);
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        final var snap = menuItem.getSnapshot(1);
        return MenuItemDto.create(snap.getId(), snap.getName(), snap.getPrice(), snap.getStatus());
    }
}
