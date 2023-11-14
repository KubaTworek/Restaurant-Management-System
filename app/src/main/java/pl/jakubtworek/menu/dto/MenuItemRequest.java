package pl.jakubtworek.menu.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record MenuItemRequest(
        @NotBlank(message = "Name cannot be null.") String name,
        @NotBlank(message = "Price cannot be null.") BigDecimal price,
        @NotBlank(message = "Menu cannot be null.") String menu
) {}
