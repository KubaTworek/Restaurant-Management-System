package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.vo.OrderId;

@JsonDeserialize(as = OrderMenuItemDto.DeserializationImpl.class)
public interface OrderMenuItemDto {

    static OrderMenuItemDto create(final Long id,
                                   final OrderId orderId,
                                   final MenuItemId menuItemId
    ) {
        return new OrderMenuItemDto.DeserializationImpl(id, orderId, menuItemId);
    }

    Long getId();

    OrderId getOrderId();

    MenuItemId getMenuItemId();

    class DeserializationImpl implements OrderMenuItemDto {
        private final Long id;
        private final OrderId orderId;
        private final MenuItemId menuItemId;

        DeserializationImpl(final Long id,
                            final OrderId orderId,
                            final MenuItemId menuItemId
        ) {
            this.id = id;
            this.orderId = orderId;
            this.menuItemId = menuItemId;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public OrderId getOrderId() {
            return orderId;
        }

        @Override
        public MenuItemId getMenuItemId() {
            return menuItemId;
        }
    }
}
