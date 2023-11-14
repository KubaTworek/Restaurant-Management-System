package pl.jakubtworek.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public record LoginResponse(
        @JsonProperty("username") String username,
        @JsonProperty("token") String token,
        @JsonProperty("tokenExpirationDate") Long tokenExpirationDate
) {}
