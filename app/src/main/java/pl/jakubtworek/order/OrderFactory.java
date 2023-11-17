package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.util.List;

class OrderFactory {
    static Order createOrder(String typeOfOrder, Long userId, List<MenuItemDto> menuItems) {
        final var orderItems = OrderItemFactory.from(menuItems);

        final var order = new Order();
        order.updateInfo(
                orderItems,
                typeOfOrder,
                new UserId(userId)
        );

        return order;
    }
}
