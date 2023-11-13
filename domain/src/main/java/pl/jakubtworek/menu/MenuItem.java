package pl.jakubtworek.menu;

import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class MenuItem {
    private Long id;
    private String name;
    private int price;
    private Menu menu;
    private Set<OrderId> orders = new HashSet<>();

    public MenuItem() {
    }

    private MenuItem(final Long id,
                     final String name,
                     final int price,
                     final Menu menu,
                     final Set<OrderId> orders
    ) {
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
                snapshot.getOrders()
        );
    }

    MenuItemSnapshot getSnapshot() {
        return new MenuItemSnapshot(
                id,
                name,
                price,
                menu.getSnapshot(),
                orders
        );
    }

    void createWithMenu(String name, int price, Long menuId, String menuName) {
        this.name = name;
        this.price = price;
        final var menu = new Menu();
        menu.updateInfo(menuId, menuName);
        this.menu = menu;
    }

    void createWithMenu(String name, int price, Menu menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
    }

    static class Menu {
        private Long id;
        private String name;
        private Set<MenuItem> menuItems = new HashSet<>();

        Menu() {
        }

        private Menu(final Long id, final String name, final Set<MenuItem> menuItems) {
            this.id = id;
            this.name = name;
            this.menuItems = menuItems;
        }

        static Menu restore(MenuSnapshot snapshot) {
            return new Menu(
                    snapshot.getId(),
                    snapshot.getName(),
                    snapshot.getMenuItems().stream().map(MenuItem::restore).collect(Collectors.toSet())
            );
        }

        MenuSnapshot getSnapshot() {
            return new MenuSnapshot(
                    id,
                    name,
                    menuItems.stream().map(MenuItem::getSnapshot).collect(Collectors.toSet())
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