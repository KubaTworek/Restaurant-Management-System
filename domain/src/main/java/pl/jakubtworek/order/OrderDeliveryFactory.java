package pl.jakubtworek.order;

import pl.jakubtworek.order.vo.Address;

class OrderDeliveryFactory {
    static OrderDelivery from(Address address) {
        final var orderDelivery = new OrderDelivery();
        orderDelivery.setAddress(address);
        return orderDelivery;
    }
}
