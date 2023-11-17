package pl.jakubtworek.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(
        @NotBlank(message = "First name cannot be null.") String username,
        @NotBlank(message = "Last name cannot be null.") String password
) { }
