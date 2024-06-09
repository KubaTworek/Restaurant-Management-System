package pl.jakubtworek.order;

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

    void addTip(final BigDecimal tip) {
        this.tip = tip;
    }

    private void calculateMinimumBasketFee() {
        if (this.price.compareTo(BigDecimal.valueOf(30)) < 0)  {
            this.minimumBasketFee = BigDecimal.valueOf(30).subtract(this.price.add(this.deliveryFee));
        } else {
            this.minimumBasketFee = BigDecimal.ONE;
        }
    }
}
