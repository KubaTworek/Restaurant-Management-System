package pl.jakubtworek.menu;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class MenuItem {
    static MenuItem restore(MenuItemSnapshot snapshot) {
        return new MenuItem(
                snapshot.getId(),
                snapshot.getName(),
                snapshot.getPrice(),
                Menu.restore(snapshot.getMenu()),
                snapshot.getOrders().stream().map(SimpleOrder::restore).collect(Collectors.toList())
        );
    }

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

    MenuItemSnapshot getSnapshot() {
        return new MenuItemSnapshot(
                id,
                name,
                price,
                menu.getSnapshot(),
                orders.stream().map(SimpleOrder::getSnapshot).collect(Collectors.toSet())
        );
    }

    void updateInfo(String name, int price, Menu menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
    }
}