package pl.jakubtworek.menu.dto;

import javax.validation.constraints.NotNull;

public class MenuRequest {
    @NotNull(message = "Menu name cannot be null.")
    private String name;

    public String getName() {
        return name;
    }
}