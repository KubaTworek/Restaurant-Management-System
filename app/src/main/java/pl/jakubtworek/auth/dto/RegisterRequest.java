package pl.jakubtworek.auth.dto;

import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {
    @NotBlank(message = "First name cannot be null.")
    private final String username;
    @NotBlank(message = "Last name cannot be null.")
    private final String password;

    public RegisterRequest(final String username, final String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
