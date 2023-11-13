package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

class MenuItemFactory {
    private final MenuItemRepository menuItemRepository;

    MenuItemFactory(final MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    MenuItem createMenuItem(MenuItemRequest toSave, MenuDto menuDto) {
        MenuItem menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                toSave.getPrice(),
                menuDto.getId(),
                menuDto.getName()
        );
        return menuItem;
    }

    MenuItem createMenuItemWithMenu(final MenuItemRequest toSave) {
        final var menu = createMenu(toSave.getMenu());
        final var created = menuItemRepository.save(menu);
        MenuItem menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                toSave.getPrice(),
                created
        );
        return menuItem;
    }

    private MenuItem.Menu createMenu(String newMenuName) {
        final var menu = new MenuItem.Menu();
        menu.updateName(newMenuName);
        return menu;
    }
}
