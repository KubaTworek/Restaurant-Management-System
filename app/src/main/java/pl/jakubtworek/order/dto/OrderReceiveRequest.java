package pl.jakubtworek.order.dto;

import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record OrderReceiveRequest(
        @NotBlank(message = "Order id cannot be null.") Long orderId,
        BigDecimal tip
) {
}
