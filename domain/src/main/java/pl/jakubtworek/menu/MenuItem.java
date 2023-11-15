package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.order.vo.OrderId;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class MenuItem {
    private Long id;
    private String name;
    private Money price;
    private Menu menu;
    private Status status;
    private Set<OrderId> orders = new HashSet<>();

    public MenuItem() {
    }

    private MenuItem(final Long id,
                     final String name,
                     final Money price,
                     final Menu menu,
                     final Status status,
                     final Set<OrderId> orders
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = status;
        this.orders = orders;
    }

    static MenuItem restore(MenuItemSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new MenuItem(
                    snapshot.getId(),
                    snapshot.getName(),
                    new Money(snapshot.getPrice()),
                    null,
                    snapshot.getStatus(),
                    snapshot.getOrders()
            );
        }
        return new MenuItem(
                snapshot.getId(),
                snapshot.getName(),
                new Money(snapshot.getPrice()),
                Menu.restore(snapshot.getMenu(), depth - 1),
                snapshot.getStatus(),
                snapshot.getOrders()
        );
    }

    MenuItemSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new MenuItemSnapshot(id, name, price.getAmount(), null, status, orders);
        }

        return new MenuItemSnapshot(
                id,
                name,
                price.getAmount(),
                menu != null ? menu.getSnapshot(depth - 1) : null,
                status,
                orders
        );
    }

    void update(Long id, String name, Money price, Long menuId, String menuName) {
        this.id = id;
        this.name = name;
        this.price = price;
        final var menu = new Menu();
        menu.updateInfo(menuId, menuName);
        this.menu = menu;
        this.status = Status.ACTIVE;
    }

    void update(Long id, String name, Money price, Menu menu) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = Status.ACTIVE;
    }

    void createWithMenu(String name, Money price, Long menuId, String menuName) {
        this.name = name;
        this.price = price;
        final var menu = new Menu();
        menu.updateInfo(menuId, menuName);
        this.menu = menu;
        this.status = Status.ACTIVE;
    }

    void createWithMenu(String name, Money price, Menu menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = Status.ACTIVE;
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

        static Menu restore(MenuSnapshot snapshot, int depth) {
            if (depth <= 0) {
                return new Menu(
                        snapshot.getId(),
                        snapshot.getName(),
                        Collections.emptySet()
                );
            }
            return new Menu(
                    snapshot.getId(),
                    snapshot.getName(),
                    snapshot.getMenuItems().stream().map(mi -> MenuItem.restore(mi, depth - 1)).collect(Collectors.toSet())
            );
        }

        MenuSnapshot getSnapshot(int depth) {
            if (depth <= 0) {
                return new MenuSnapshot(id, name, Collections.emptySet());
            }

            return new MenuSnapshot(
                    id,
                    name,
                    menuItems.stream().map(item -> item.getSnapshot(depth - 1)).collect(Collectors.toSet())
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
