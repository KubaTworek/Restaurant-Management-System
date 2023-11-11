package pl.jakubtworek.employee;

import pl.jakubtworek.order.dto.TypeOfOrder;

class Order {
    private final Long orderId;
    private final TypeOfOrder orderType;
    private final Integer amountOfMenuItems;

    Order(final Long orderId, final TypeOfOrder orderType, final Integer amountOfMenuItems) {
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
