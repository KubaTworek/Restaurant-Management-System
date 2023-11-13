package pl.jakubtworek.order.vo;

import java.io.Serializable;

public class OrderMenuItemId implements Serializable {
    private Long orderId;
    private Long menuItemId;

    public OrderMenuItemId() {
        // konstruktor domyślny potrzebny do JPA
    }

    public OrderMenuItemId(Long orderId, Long menuItemId) {
        this.orderId = orderId;
        this.menuItemId = menuItemId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getMenuItemId() {
        return menuItemId;
    }

}
