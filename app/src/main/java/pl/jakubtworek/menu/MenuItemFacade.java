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

    MenuItemFacade(final MenuItemRepository menuItemRepository, final MenuItemQueryRepository menuItemQueryRepository,
                   final MenuQueryRepository menuQueryRepository) {
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
        final var menuDto = menuQueryRepository.findDtoByName(toSave.getMenu())
                .orElseThrow(() -> new IllegalStateException("There are no menu in restaurant with that name: " + toSave.getMenu()));

        final var menu = new Menu();
        menu.setId(menuDto.getId());
        menu.setName(menuDto.getName());

        final var menuItem = new MenuItem();
        menuItem.setName(toSave.getName());
        menuItem.setPrice(toSave.getPrice());
        menuItem.setMenu(menu);

        return toDto(menuItemRepository.save(menuItem));
    }

    void deleteById(Long id) {
        menuItemRepository.deleteById(id);
    }

    List<MenuItemDto> findByMenu(String menuName) {
        return menuItemQueryRepository.findByMenuName(menuName);
    }

    private MenuItemDto toDto(MenuItem menuItem) {
        return MenuItemDto.create(menuItem.getId(), menuItem.getName(), menuItem.getPrice());
    }
}