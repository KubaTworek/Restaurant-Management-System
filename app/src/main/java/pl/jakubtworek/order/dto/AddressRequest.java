package pl.jakubtworek.order.dto;

import jakarta.validation.constraints.NotBlank;

public record AddressRequest(
        @NotBlank(message = "District cannot be null.") String district,
        @NotBlank(message = "street cannot be null.") String street,
        @NotBlank(message = "houseNumber cannot be null.") String houseNumber
) {
}
