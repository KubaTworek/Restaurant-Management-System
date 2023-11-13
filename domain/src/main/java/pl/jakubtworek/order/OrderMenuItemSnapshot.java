package pl.jakubtworek.order;

import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.vo.OrderId;

public class OrderMenuItemSnapshot {
    private Long id;
    private OrderId orderId;
    private MenuItemId menuItemId;

    public OrderMenuItemSnapshot() {
    }

    OrderMenuItemSnapshot(final Long id,
                          final OrderId orderId,
                          final MenuItemId menuItem
    ) {
        this.id = id;
        this.orderId = orderId;
        this.menuItemId = menuItem;
    }

    Long getId() {
        return id;
    }

    OrderId getOrderId() {
        return orderId;
    }

    MenuItemId getMenuItemId() {
        return menuItemId;
    }
}
