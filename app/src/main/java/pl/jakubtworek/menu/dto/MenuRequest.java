package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuRequest {
    @NotNull(message = "Menu name cannot be null.")
    private final String name;

    public MenuRequest(final String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}