package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

public class MenuItemRequest {
    @NotNull(message = "Name cannot be null.")
    private final String name;
    @NotNull(message = "Price cannot be null.")
    private final BigDecimal price;
    @NotNull(message = "Menu cannot be null.")
    private final String menu;

    public MenuItemRequest(final String name, final BigDecimal price, final String menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public String getMenu() {
        return menu;
    }
}
