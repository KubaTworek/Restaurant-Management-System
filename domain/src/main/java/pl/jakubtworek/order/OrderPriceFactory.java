package pl.jakubtworek.order;

import pl.jakubtworek.order.vo.Address;

import java.math.BigDecimal;

class OrderPriceFactory {
    static OrderPrice from(BigDecimal price) {
        final var orderPrice = new OrderPrice();
        orderPrice.calculatePrice(price);
        return orderPrice;
    }

    static OrderPrice from(BigDecimal price, Address address) {
        final var orderPrice = new OrderPrice();
        orderPrice.calculatePrice(price, address);
        return orderPrice;
    }
}
