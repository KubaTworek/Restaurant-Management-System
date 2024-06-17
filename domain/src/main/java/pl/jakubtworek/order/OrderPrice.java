package pl.jakubtworek.order;

import pl.jakubtworek.order.vo.Address;

import java.math.BigDecimal;

class OrderPrice {
    private Long id;
    private BigDecimal price;
    private BigDecimal deliveryFee;
    private BigDecimal minimumBasketFee;
    private BigDecimal tip;

    OrderPrice() {
    }

    OrderPrice(final Long id, final BigDecimal price, final BigDecimal deliveryFee, final BigDecimal minimumBasketFee, final BigDecimal tip) {
        this.id = id;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.minimumBasketFee = minimumBasketFee;
        this.tip = tip;
    }

    static OrderPrice restore(OrderPriceSnapshot snapshot) {
        return new OrderPrice(
                snapshot.getId(),
                snapshot.getPrice(),
                snapshot.getDeliveryFee(),
                snapshot.getMinimumBasketFee(),
                snapshot.getTip()
        );
    }

    OrderPriceSnapshot getSnapshot() {
        return new OrderPriceSnapshot(
                id,
                price,
                deliveryFee,
                minimumBasketFee,
                tip
        );
    }

    void calculatePrice(final BigDecimal price) {
        this.price = price;
        this.deliveryFee = BigDecimal.ZERO;
        calculateMinimumBasketFee();
        this.tip = BigDecimal.ZERO;
    }

    void calculatePrice(final BigDecimal price, Address address) {
        this.price = price;
        this.deliveryFee = address.getDistrict().getDeliveryFee();
        calculateMinimumBasketFee();
        this.tip = BigDecimal.ZERO;
    }

    void addTip(final BigDecimal tip) {
        this.tip = tip;
    }

    private void calculateMinimumBasketFee() {
        final var fullPrice = this.price.add(this.deliveryFee);
        if (fullPrice.compareTo(BigDecimal.valueOf(30)) < 0)  {
            this.minimumBasketFee = BigDecimal.valueOf(30).subtract(fullPrice);
        } else {
            this.minimumBasketFee = BigDecimal.ZERO;
        }
    }
}
