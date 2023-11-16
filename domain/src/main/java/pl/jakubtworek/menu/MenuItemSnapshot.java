package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Status;

import java.math.BigDecimal;

class MenuItemSnapshot {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuSnapshot menu;
    private Status status;

    public MenuItemSnapshot() {
    }

    MenuItemSnapshot(final Long id,
                     final String name,
                     final BigDecimal price,
                     final MenuSnapshot menu,
                     final Status status
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
        this.status = status;
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

    Status getStatus() {
        return status;
    }
}
