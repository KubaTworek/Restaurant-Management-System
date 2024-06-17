package pl.jakubtworek.order.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record OrderRequest(
        @NotBlank(message = "Type of order cannot be null.") String typeOfOrder,
        @NotBlank(message = "Menu items cannot be null.") List<String> menuItems,
        AddressRequest address
) {
}
