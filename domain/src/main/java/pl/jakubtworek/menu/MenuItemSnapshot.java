package pl.jakubtworek.menu;

import pl.jakubtworek.common.vo.Status;

import java.math.BigDecimal;
import java.util.Objects;

class MenuItemSnapshot {
    private Long id;
    private String name;
    private BigDecimal price;
    private MenuSnapshot menu;
    private Status status;

    MenuItemSnapshot() {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuItemSnapshot that = (MenuItemSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(menu, that.menu) && status == that.status;
    }
}
