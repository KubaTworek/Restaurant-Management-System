package pl.jakubtworek.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record RegisterRequest(@NotBlank(message = "First name cannot be null.") String username,
                              @NotBlank(message = "Last name cannot be null.") String password,
                              @NotBlank(message = "Role cannot be null.") String role,
                              String firstName,
                              String lastName,
                              String email,
                              String phone
) { }
