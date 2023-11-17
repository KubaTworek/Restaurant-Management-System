package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.common.vo.Status;

import java.util.Objects;

class MenuItem {
    private Long id;
    private String name;
    private Money price;
    private Menu menu;
    private Status status;

    MenuItem() {
    }

    private MenuItem(final Long id,
                     final String name,
                     final Money price,
                     final Menu menu,
                     final Status status
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = status;
    }

    static MenuItem restore(MenuItemSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new MenuItem(
                    snapshot.getId(),
                    snapshot.getName(),
                    new Money(snapshot.getPrice()),
                    null,
                    snapshot.getStatus()
            );
        }
        return new MenuItem(
                snapshot.getId(),
                snapshot.getName(),
                new Money(snapshot.getPrice()),
                Menu.restore(snapshot.getMenu(), depth - 1),
                snapshot.getStatus()
        );
    }

    MenuItemSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new MenuItemSnapshot(id, name, price.value(), null, status);
        }

        return new MenuItemSnapshot(id, name, price.value(), menu != null ? menu.getSnapshot(depth - 1) : null, status);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuItem menuItem = (MenuItem) o;
        return Objects.equals(id, menuItem.id) && Objects.equals(name, menuItem.name) && Objects.equals(price, menuItem.price) && Objects.equals(menu, menuItem.menu) && status == menuItem.status;
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
        this.menu.addMenuItem(this);
        this.status = Status.ACTIVE;
    }
}
