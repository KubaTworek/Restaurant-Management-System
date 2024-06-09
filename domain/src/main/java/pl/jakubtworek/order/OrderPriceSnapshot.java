package pl.jakubtworek.order;

import java.math.BigDecimal;
import java.util.Objects;

class OrderPriceSnapshot {
    private Long id;
    private BigDecimal price;
    private BigDecimal deliveryFee;
    private BigDecimal minimumBasketFee;
    private BigDecimal tip;

    OrderPriceSnapshot() {
    }

    OrderPriceSnapshot(final Long id,
                       final BigDecimal price,
                       final BigDecimal deliveryFee,
                       final BigDecimal minimumBasketFee,
                       final BigDecimal tip
    ) {
        this.id = id;
        this.price = price;
        this.deliveryFee = deliveryFee;
        this.minimumBasketFee = minimumBasketFee;
        this.tip = tip;
    }

    Long getId() {
        return id;
    }

    BigDecimal getPrice() {
        return price;
    }

    BigDecimal getDeliveryFee() {
        return deliveryFee;
    }

    BigDecimal getMinimumBasketFee() {
        return minimumBasketFee;
    }

    BigDecimal getTip() {
        return tip;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderPriceSnapshot that = (OrderPriceSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(price, that.price) && Objects.equals(deliveryFee, that.deliveryFee) && Objects.equals(minimumBasketFee, that.minimumBasketFee) && Objects.equals(tip, that.tip);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, price, deliveryFee, minimumBasketFee, tip);
    }
}
