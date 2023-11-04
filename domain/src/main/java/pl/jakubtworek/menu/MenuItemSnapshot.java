package pl.jakubtworek.menu;

import pl.jakubtworek.order.dto.SimpleOrderSnapshot;

import java.util.HashSet;
import java.util.Set;

class MenuItemSnapshot {
    private Long id;
    private String name;
    private int price;
    private MenuSnapshot menu;
    private Set<SimpleOrderSnapshot> orders = new HashSet<>();

    public MenuItemSnapshot() {
    }

    MenuItemSnapshot(final Long id, final String name, final int price, final MenuSnapshot menu, final Set<SimpleOrderSnapshot> orders) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.orders = orders;
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    MenuSnapshot getMenu() {
        return menu;
    }

    Set<SimpleOrderSnapshot> getOrders() {
        return orders;
    }
}
