package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

class MenuItemFactory {

    public static MenuItem createMenuItem(MenuItemRequest toSave, MenuDto menuDto) {
        MenuItem menuItem = new MenuItem();
        menuItem.createWithMenu(
                toSave.getName(),
                toSave.getPrice(),
                menuDto.getId(),
                menuDto.getName()
        );
        return menuItem;
    }

    public static MenuItem.Menu createMenu(String newMenuName) {
        final var menu = new MenuItem.Menu();
        menu.updateName(newMenuName);
        return menu;
    }
}
