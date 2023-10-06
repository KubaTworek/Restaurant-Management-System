package pl.jakubtworek.restaurant.auth;

import javax.validation.constraints.NotNull;

class LoginRequest {
    @NotNull(message = "First name cannot be null.")
    private String username;
    @NotNull(message = "Last name cannot be null.")
    private String password;

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
