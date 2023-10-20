package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuItemRequest {
    @NotNull(message = "Name cannot be null.")
    private final String name;
    @NotNull(message = "Price cannot be null.")
    private final int price;
    @NotNull(message = "Menu cannot be null.")
    private final String menu;

    public MenuItemRequest(final String name, final int price, final String menu) {
        this.name = name;
        this.price = price;
        this.menu = menu;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getMenu() {
        return menu;
    }
}