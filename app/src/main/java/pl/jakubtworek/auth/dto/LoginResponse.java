package pl.jakubtworek.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LoginResponse {
    @JsonProperty("username")
    private final String username;
    @JsonProperty("token")
    private final String token;
    @JsonProperty("tokenExpirationDate")
    private final Long tokenExpirationDate;

    public LoginResponse(final String username, final String token, final Long tokenExpirationDate) {
        this.username = username;
        this.token = token;
        this.tokenExpirationDate = tokenExpirationDate;
    }
}
