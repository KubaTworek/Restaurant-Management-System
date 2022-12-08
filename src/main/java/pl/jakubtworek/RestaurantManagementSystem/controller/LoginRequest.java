package pl.jakubtworek.RestaurantManagementSystem.controller;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {
    @NotNull(message = "First name cannot be null.")
    private String username;
    @NotNull(message = "Last name cannot be null.")
    private String password;
    @NotNull(message = "Job cannot be null.")
    private String role;
}
