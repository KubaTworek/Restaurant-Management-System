package pl.jakubtworek.auth.dto;

import javax.validation.constraints.NotNull;

public class LoginRequest {
    @NotNull(message = "First name cannot be null.")
    private String username;
    @NotNull(message = "Last name cannot be null.")
    private String password;

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}