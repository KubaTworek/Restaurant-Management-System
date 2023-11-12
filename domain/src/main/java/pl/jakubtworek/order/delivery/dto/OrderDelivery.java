package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.order.dto.TypeOfOrder;

public class OrderDelivery {
    private final Long orderId;
    private final TypeOfOrder orderType;
    private final Integer amountOfMenuItems;

    public OrderDelivery(final Long orderId, final TypeOfOrder orderType, final Integer amountOfMenuItems) {
        this.orderId = orderId;
        this.orderType = orderType;
        this.amountOfMenuItems = amountOfMenuItems;
    }

    public Long getOrderId() {
        return orderId;
    }

    public TypeOfOrder getOrderType() {
        return orderType;
    }

    public Integer getAmountOfMenuItems() {
        return amountOfMenuItems;
    }
}
