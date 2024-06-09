package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.order.vo.OrderStatus;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;

@JsonDeserialize(as = OrderDto.DeserializationImpl.class)
public interface OrderDto {

    static OrderDto create(final Long id,
                           final OrderPriceDto price,
                           final ZonedDateTime hourOrder,
                           final ZonedDateTime hourPrepared,
                           final ZonedDateTime hourReceived,
                           final TypeOfOrder typeOfOrder,
                           final OrderStatus status,
                           final OrderDeliveryDto delivery,
                           final List<OrderItemDto> orderItems
    ) {
        return new OrderDto.DeserializationImpl(id, price, hourOrder, hourPrepared, hourReceived, typeOfOrder, status, delivery, orderItems);
    }

    Long getId();

    OrderPriceDto getPrice();

    ZonedDateTime getHourOrder();

    ZonedDateTime getHourPrepared();

    ZonedDateTime getHourReceived();

    TypeOfOrder getTypeOfOrder();

    OrderStatus getStatus();
    OrderDeliveryDto getDelivery();

    List<OrderItemDto> getOrderItems();

    class DeserializationImpl implements OrderDto {
        private final Long id;
        private final OrderPriceDto price;
        private final ZonedDateTime hourOrder;
        private final ZonedDateTime hourPrepared;
        private final ZonedDateTime hourReceived;
        private final TypeOfOrder typeOfOrder;
        private final OrderStatus status;
        private final OrderDeliveryDto delivery;
        private final List<OrderItemDto> orderItems;

        DeserializationImpl(final Long id,
                            final OrderPriceDto price,
                            final ZonedDateTime hourOrder,
                            final ZonedDateTime hourPrepared,
                            final ZonedDateTime hourReceived,
                            final TypeOfOrder typeOfOrder,
                            final OrderStatus status,
                            final OrderDeliveryDto delivery,
                            final List<OrderItemDto> orderItems) {
            this.id = id;
            this.price = price;
            this.hourOrder = hourOrder;
            this.hourPrepared = hourPrepared;
            this.hourReceived = hourReceived;
            this.typeOfOrder = typeOfOrder;
            this.status = status;
            this.delivery = delivery;
            this.orderItems = orderItems;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public OrderPriceDto getPrice() {
            return price;
        }

        @Override
        public ZonedDateTime getHourOrder() {
            return hourOrder;
        }

        @Override
        public ZonedDateTime getHourPrepared() {
            return hourPrepared;
        }

        @Override
        public ZonedDateTime getHourReceived() {
            return hourReceived;
        }

        @Override
        public TypeOfOrder getTypeOfOrder() {
            return typeOfOrder;
        }

        @Override
        public OrderStatus getStatus() {
            return status;
        }

        @Override
        public OrderDeliveryDto getDelivery() {
            return delivery;
        }

        @Override
        public List<OrderItemDto> getOrderItems() {
            return orderItems;
        }
    }
}
