package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;

@JsonDeserialize(as = OrderDto.DeserializationImpl.class)
public interface OrderDto {

    static OrderDto create(final Long id,
                           final BigDecimal price,
                           final ZonedDateTime hourOrder,
                           final ZonedDateTime hourAway,
                           final TypeOfOrder typeOfOrder,
                           final List<OrderMenuItemDto> menuItems
    ) {
        return new OrderDto.DeserializationImpl(id, price, hourOrder, hourAway, typeOfOrder, menuItems);
    }

    Long getId();

    BigDecimal getPrice();

    ZonedDateTime getHourOrder();

    ZonedDateTime getHourAway();

    TypeOfOrder getTypeOfOrder();

    List<OrderMenuItemDto> getMenuItems();

    class DeserializationImpl implements OrderDto {
        private final Long id;
        private final BigDecimal price;
        private final ZonedDateTime hourOrder;
        private final ZonedDateTime hourAway;
        private final TypeOfOrder typeOfOrder;
        private final List<OrderMenuItemDto> menuItems;

        DeserializationImpl(final Long id,
                            final BigDecimal price,
                            final ZonedDateTime hourOrder,
                            final ZonedDateTime hourAway,
                            final TypeOfOrder typeOfOrder,
                            final List<OrderMenuItemDto> menuItems) {
            this.id = id;
            this.price = price;
            this.hourOrder = hourOrder;
            this.hourAway = hourAway;
            this.typeOfOrder = typeOfOrder;
            this.menuItems = menuItems;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public ZonedDateTime getHourOrder() {
            return hourOrder;
        }

        @Override
        public ZonedDateTime getHourAway() {
            return hourAway;
        }

        @Override
        public TypeOfOrder getTypeOfOrder() {
            return typeOfOrder;
        }

        @Override
        public List<OrderMenuItemDto> getMenuItems() {
            return menuItems;
        }
    }
}
