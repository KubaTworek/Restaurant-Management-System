package pl.jakubtworek.order.dto;

import jakarta.validation.constraints.NotBlank;

public record OrderConfirmRequest(
        @NotBlank(message = "Order id cannot be null.") Long orderId,
        @NotBlank(message = "Decision cannot be null.") String decision
) {
}
