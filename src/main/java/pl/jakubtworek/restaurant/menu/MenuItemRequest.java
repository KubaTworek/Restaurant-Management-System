package pl.jakubtworek.restaurant.menu;

import javax.validation.constraints.NotNull;

class MenuItemRequest {
    @NotNull(message = "Name cannot be null.")
    private String name;
    @NotNull(message = "Price cannot be null.")
    private double price;
    @NotNull(message = "Menu cannot be null.")
    private String menu;

    String getMenu() {
        return menu;
    }
}