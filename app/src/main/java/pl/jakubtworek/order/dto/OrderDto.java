package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.order.vo.TypeOfOrder;

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
                           final List<OrderItemDto> orderItems
    ) {
        return new OrderDto.DeserializationImpl(id, price, hourOrder, hourAway, typeOfOrder, orderItems);
    }

    Long getId();

    BigDecimal getPrice();

    ZonedDateTime getHourOrder();

    ZonedDateTime getHourAway();

    TypeOfOrder getTypeOfOrder();

    List<OrderItemDto> getOrderItems();

    class DeserializationImpl implements OrderDto {
        private final Long id;
        private final BigDecimal price;
        private final ZonedDateTime hourOrder;
        private final ZonedDateTime hourAway;
        private final TypeOfOrder typeOfOrder;
        private final List<OrderItemDto> orderItems;

        DeserializationImpl(final Long id,
                            final BigDecimal price,
                            final ZonedDateTime hourOrder,
                            final ZonedDateTime hourAway,
                            final TypeOfOrder typeOfOrder,
                            final List<OrderItemDto> orderItems) {
            this.id = id;
            this.price = price;
            this.hourOrder = hourOrder;
            this.hourAway = hourAway;
            this.typeOfOrder = typeOfOrder;
            this.orderItems = orderItems;
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
        public List<OrderItemDto> getOrderItems() {
            return orderItems;
        }
    }
}
