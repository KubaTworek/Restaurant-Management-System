package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.order.vo.TypeOfOrder;

public record OrderDelivery(
        Long orderId,
        TypeOfOrder orderType,
        Integer amountOfMenuItems
) {
}
