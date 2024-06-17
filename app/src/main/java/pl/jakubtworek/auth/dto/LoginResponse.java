package pl.jakubtworek.auth.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import pl.jakubtworek.common.vo.Role;

public record LoginResponse(
        @JsonProperty("username") String username,
        @JsonProperty("role") Role role,
        @JsonProperty("token") String token,
        @JsonProperty("refreshToken") String refreshToken,
        @JsonProperty("tokenExpirationDate") Long tokenExpirationDate,
        @JsonProperty("refreshTokenExpirationDate") Long refreshTokenExpirationDate
) { }
