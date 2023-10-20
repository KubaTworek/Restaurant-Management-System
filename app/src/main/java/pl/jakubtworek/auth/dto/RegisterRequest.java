package pl.jakubtworek.auth.dto;

import javax.validation.constraints.NotNull;

public class RegisterRequest {
    @NotNull(message = "First name cannot be null.")
    private final String username;
    @NotNull(message = "Last name cannot be null.")
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
