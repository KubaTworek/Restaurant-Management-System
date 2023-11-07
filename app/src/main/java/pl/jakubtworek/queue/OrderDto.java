package pl.jakubtworek.queue;

import pl.jakubtworek.order.dto.TypeOfOrder;

class OrderDto {
    private final Long orderId;
    private final TypeOfOrder orderType;
    private final Integer amountOfMenuItems;

    OrderDto(final Long orderId, final TypeOfOrder orderType, final Integer amountOfMenuItems) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.amountOfMenuItems = amountOfMenuItems;
    }

    Long getOrderId() {
        return orderId;
    }

    TypeOfOrder getOrderType() {
        return orderType;
    }

    Integer getAmountOfMenuItems() {
        return amountOfMenuItems;
    }
}
