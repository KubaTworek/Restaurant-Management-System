package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.order.dto.TypeOfOrder;

public record OrderDelivery(
        Long orderId,
        TypeOfOrder orderType,
        Integer amountOfMenuItems
) {}
