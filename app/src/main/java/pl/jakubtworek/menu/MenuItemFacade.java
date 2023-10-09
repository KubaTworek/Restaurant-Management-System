package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.util.List;
import java.util.Optional;

public class MenuItemFacade {
    private final MenuFacade menuFacade;
    private final MenuItemRepository menuItemRepository;
    private final MenuItemQueryRepository menuItemQueryRepository;
    private final MenuQueryRepository menuQueryRepository;

    MenuItemFacade(final MenuFacade menuFacade, final MenuItemRepository menuItemRepository,
                   final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository) {
        this.menuFacade = menuFacade;
        this.menuItemRepository = menuItemRepository;
        this.menuItemQueryRepository = menuItemQueryRepository;
        this.menuQueryRepository = menuQueryRepository;
    }

    Optional<MenuItemDto> findById(Long theId) {
        return menuItemQueryRepository.findDtoById(theId);
    }

    MenuItemDto save(MenuItemRequest toSave) {
        Menu menu = menuQueryRepository.findByName(toSave.getMenu())
                .orElseThrow(() -> new IllegalStateException("There are no menu in restaurant with that name: " + toSave.getMenu()));

        MenuItem menuItem = new MenuItem();
        menuItem.setMenu(menu);

        return toDto(menuItemRepository.save(menuItem));
    }

    MenuItemDto update(Long menuItemId, MenuItemRequest newMenuItem) {
        // todo:
        return null;
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findDtoByMenuName(menuName);
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        return MenuItemDto.builder()
                .withId(menuItem.getId())
                .withName(menuItem.getName())
                .withPrice(menuItem.getPrice())
                .withMenu(menuFacade.toDto(menuItem.getMenu()))
                .build();
    }
}