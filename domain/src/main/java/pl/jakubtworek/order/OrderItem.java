package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.ItemDto;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.Set;

class OrderItem {
    private Long id;
    private String name;
    private BigDecimal price;
    private Integer amount;
    private Order order;

    OrderItem() {
    }

    private OrderItem(final Long id,
                      final String name,
                      final BigDecimal price,
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
                    snapshot.getPrice(),
                    snapshot.getAmount(),
                    null
            );
        }
        return new OrderItem(
                snapshot.getId(),
                snapshot.getName(),
                snapshot.getPrice(),
                snapshot.getAmount(),
                Order.restore(snapshot.getOrder(), depth - 1)
        );
    }

    OrderItemSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new OrderItemSnapshot(id, name, price, amount, null);
        }

        return new OrderItemSnapshot(
                id,
                name,
                price,
                amount,
                order != null ? order.getSnapshot(depth - 1) : null
        );
    }

    void setInfo(String name, Set<ItemDto> items, Integer amount) {
        this.name = name;
        this.price = getPriceForItem(name, items);
        this.amount = amount;
    }

    void setOrder(final Order order) {
        this.order = order;
    }

    BigDecimal calculatePrice() {
        if (price != null && amount != null) {
            return price.multiply(BigDecimal.valueOf(amount));
        } else {
            return BigDecimal.ZERO;
        }
    }

    private BigDecimal getPriceForItem(String itemName, Set<ItemDto> menuItems) {
        return menuItems.stream()
                .filter(mi -> mi.getName().equals(itemName))
                .findFirst()
                .map(ItemDto::getPrice)
                .orElse(BigDecimal.valueOf(0));
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderItem orderItem = (OrderItem) o;
        return Objects.equals(id, orderItem.id) && Objects.equals(name, orderItem.name) && Objects.equals(price, orderItem.price) && Objects.equals(amount, orderItem.amount) && Objects.equals(order, orderItem.order);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price, amount, order);
    }
}
