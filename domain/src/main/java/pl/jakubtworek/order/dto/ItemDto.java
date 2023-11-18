package pl.jakubtworek.order.dto;

import java.math.BigDecimal;

public class ItemDto {
    private final String name;
    private final BigDecimal price;

    public ItemDto(final String name, final BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
