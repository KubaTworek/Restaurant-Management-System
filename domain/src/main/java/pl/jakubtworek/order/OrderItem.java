package pl.jakubtworek.order;

import pl.jakubtworek.common.vo.Money;

class OrderItem {
    private Long id;
    private String name;
    private Money price;
    private Integer amount;
    private Order order;

    public OrderItem() {
    }

    OrderItem(final Long id,
              final String name,
              final Money price,
              final Integer amount,
              final Order order
    ) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.amount = amount;
        this.order = order;
    }

    static OrderItem restore(OrderItemSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new OrderItem(
                    snapshot.getId(),
                    snapshot.getName(),
                    new Money(snapshot.getPrice()),
                    snapshot.getAmount(),
                    null
            );
        }
        return new OrderItem(
                snapshot.getId(),
                snapshot.getName(),
                new Money(snapshot.getPrice()),
                snapshot.getAmount(),
                Order.restore(snapshot.getOrder(), depth - 1)
        );
    }

    OrderItemSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new OrderItemSnapshot(id, name, price.getValue(), amount, null);
        }

        return new OrderItemSnapshot(
                id,
                name,
                price.getValue(),
                amount,
                order.getSnapshot(depth - 1)
        );
    }
}
