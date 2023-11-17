package pl.jakubtworek.order;

import java.math.BigDecimal;
import java.util.Objects;

class OrderItemSnapshot {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer amount;
    private OrderSnapshot order;

    OrderItemSnapshot() {
    }

    OrderItemSnapshot(final Long id,
                      final String name,
                      final BigDecimal price,
                      final Integer amount,
                      final OrderSnapshot order
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.order = order;
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    BigDecimal getPrice() {
        return price;
    }

    Integer getAmount() {
        return amount;
    }

    OrderSnapshot getOrder() {
        return order;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderItemSnapshot that = (OrderItemSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(price, that.price) && Objects.equals(amount, that.amount) && Objects.equals(order, that.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, amount, order);
    }
}
