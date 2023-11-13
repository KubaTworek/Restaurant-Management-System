package pl.jakubtworek.menu;

import pl.jakubtworek.order.vo.OrderId;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

class MenuItemSnapshot {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuSnapshot menu;
    private Set<OrderId> orders = new HashSet<>();

    public MenuItemSnapshot() {
    }

    MenuItemSnapshot(final Long id,
                     final String name,
                     final BigDecimal price,
                     final MenuSnapshot menu,
                     final Set<OrderId> orders
    ) {
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

    BigDecimal getPrice() {
        return price;
    }

    MenuSnapshot getMenu() {
        return menu;
    }

    Set<OrderId> getOrders() {
        return orders;
    }
}
