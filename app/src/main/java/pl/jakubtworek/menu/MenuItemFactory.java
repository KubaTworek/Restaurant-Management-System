package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.order.vo.Money;

class MenuItemFactory {
    private final MenuItemRepository menuItemRepository;

    MenuItemFactory(final MenuItemRepository menuItemRepository) {
        this.menuItemRepository = menuItemRepository;
    }

    MenuItem createMenuItem(MenuItemRequest toSave, MenuDto menuDto) {
        MenuItem menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                new Money(toSave.getPrice()),
                menuDto.getId(),
                menuDto.getName()
        );
        return menuItem;
    }

    MenuItem createMenuItemAndMenu(final MenuItemRequest toSave) {
        final var menu = createMenu(toSave.getMenu());
        final var created = menuItemRepository.save(menu);
        MenuItem menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                new Money(toSave.getPrice()),
                created
        );
        return menuItem;
    }

    MenuItem updateMenuItem(Long menuItemId, MenuItemRequest toUpdate, MenuDto menu) {
        MenuItem menuItem = new MenuItem();
        menuItem.update(
                menuItemId,
                toUpdate.getName(),
                new Money(toUpdate.getPrice()),
                menu.getId(),
                menu.getName()
        );
        return menuItem;
    }

    MenuItem updateMenuItemAndCreateMenu(Long menuItemId, MenuItemRequest toUpdate) {
        final var menu = createMenu(toUpdate.getMenu());
        final var created = menuItemRepository.save(menu);
        MenuItem menuItem = new MenuItem();
        menuItem.update(
                menuItemId,
                toUpdate.getName(),
                new Money(toUpdate.getPrice()),
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
