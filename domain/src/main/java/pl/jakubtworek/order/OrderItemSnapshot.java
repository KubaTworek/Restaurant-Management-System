package pl.jakubtworek.order;

import java.math.BigDecimal;

class OrderItemSnapshot {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer amount;
    private OrderSnapshot order;

    public OrderItemSnapshot() {
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
}
