package pl.jakubtworek.auth.dto;

public class LoginResponse {
    private final String username;
    private final String token;
    private final Long tokenExpirationDate;

    public LoginResponse(final String username, final String token, final Long tokenExpirationDate) {
        this.username = username;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
