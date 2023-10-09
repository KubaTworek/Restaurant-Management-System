package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

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

    public SimpleMenuItem getByName(String name) {
        return menuItemQueryRepository.findSimpleByName(name)
                .orElseThrow(() -> new IllegalStateException("There are no menu item in restaurant with that name: " + name));
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

    MenuItemDto update(Long menuItemId, MenuItemRequest toUpdate) {
        return menuItemRepository.findById(menuItemId)
                .map(menuItem -> {
                    menuItem.setName(toUpdate.getName());
                    menuItem.setPrice(toUpdate.getPrice());
                    menuItem.setMenu(menuFacade.getByName(toUpdate.getMenu()));
                    return toDto(menuItemRepository.save(menuItem));
                })
                .orElseThrow(() -> new IllegalStateException("There are no menu item in restaurant with that id: " + menuItemId));
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findByMenuName(menuName);
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