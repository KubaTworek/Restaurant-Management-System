package pl.jakubtworek.restaurant.auth;

class LoginResponse {
    private final String username;
    private final String token;
    private final Long tokenExpirationDate;

    LoginResponse(final String username, final String token, final Long tokenExpirationDate) {
        this.username = username;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
