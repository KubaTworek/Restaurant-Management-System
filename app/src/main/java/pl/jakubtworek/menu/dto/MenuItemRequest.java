package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuItemRequest {
    @NotNull(message = "Name cannot be null.")
    private String name;
    @NotNull(message = "Price cannot be null.")
    private int price;
    @NotNull(message = "Menu cannot be null.")
    private String menu;

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