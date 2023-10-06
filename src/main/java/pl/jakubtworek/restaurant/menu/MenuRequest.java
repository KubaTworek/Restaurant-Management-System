package pl.jakubtworek.restaurant.menu;

import javax.validation.constraints.NotNull;

class MenuRequest {
    @NotNull(message = "Menu name cannot be null.")
    private String name;

    String getName() {
        return name;
    }
}