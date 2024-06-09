package pl.jakubtworek.order;

import java.math.BigDecimal;

class OrderPriceFactory {
    static OrderPrice from(BigDecimal price) {
        final var orderPrice = new OrderPrice();
        orderPrice.calculatePrice(price);
        return orderPrice;
    }
}
