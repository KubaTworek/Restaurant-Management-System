package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuItemRequest {
    @NotNull(message = "Name cannot be null.")
    private String name;
    @NotNull(message = "Price cannot be null.")
    private double price;
    @NotNull(message = "Menu cannot be null.")
    private String menu;

    public String getMenu() {
        return menu;
    }
}