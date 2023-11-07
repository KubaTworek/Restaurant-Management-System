package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class MenuItem {
    private Long id;
    private String name;
    private int price;
    private Menu menu;
    private List<SimpleOrder> orders = new ArrayList<>();

    public MenuItem() {
    }

    private MenuItem(final Long id, final String name, final int price, final Menu menu, final List<SimpleOrder> orders) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.orders = orders;
    }

    static MenuItem restore(MenuItemSnapshot snapshot) {
        return new MenuItem(
                snapshot.getId(),
                snapshot.getName(),
                snapshot.getPrice(),
                Menu.restore(snapshot.getMenu()),
                snapshot.getOrders().stream().map(SimpleOrder::restore).collect(Collectors.toList())
        );
    }

    MenuItemSnapshot getSnapshot() {
        return new MenuItemSnapshot(
                id,
                name,
                price,
                menu.getSnapshot(),
                orders.stream().map(SimpleOrder::getSnapshot).collect(Collectors.toSet())
        );
    }

    void createWithMenu(String name, int price, Long menuId, String menuName) {
        this.name = name;
        this.price = price;
        final var menu = new Menu();
        menu.updateInfo(menuId, menuName);
        this.menu = menu;
    }

    Menu createNewMenu(final String menuName) {
        final var menu = new Menu();
        menu.updateName(menuName);
        return menu;
    }

    static class Menu {
        private Long id;
        private String name;
        private List<SimpleMenuItem> menuItems = new ArrayList<>();

        Menu() {
        }

        private Menu(final Long id, final String name, final List<SimpleMenuItem> menuItems) {
            this.id = id;
            this.name = name;
            this.menuItems = menuItems;
        }

        static Menu restore(MenuSnapshot snapshot) {
            return new Menu(
                    snapshot.getId(),
                    snapshot.getName(),
                    snapshot.getMenuItems().stream().map(SimpleMenuItem::restore).collect(Collectors.toList())
            );
        }

        MenuSnapshot getSnapshot() {
            return new MenuSnapshot(
                    id,
                    name,
                    menuItems.stream().map(SimpleMenuItem::getSnapshot).collect(Collectors.toSet())
            );
        }

        void updateInfo(Long id, String name) {
            this.id = id;
            this.name = name;
        }

        void updateName(String name) {
            this.name = name;
        }
    }
}