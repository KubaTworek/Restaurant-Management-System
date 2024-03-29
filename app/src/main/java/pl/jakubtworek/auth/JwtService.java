package pl.jakubtworek.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

class JwtService {

    private static SecretKey createSecretKey() {
        byte[] keyBytes = SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    String buildJwt(String username, long expirationDate) {
        return Jwts.builder()
                .setIssuer("Social Media")
                .setSubject("JWT Token")
                .claim("username", username)
                .claim("role", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationDate))
                .signWith(createSecretKey())
                .compact();
    }

    Claims parseJwtClaims(String jwt) {
        String jwtWithoutBearer = jwt.replaceFirst("Bearer ", "");
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(jwtWithoutBearer)
                .getBody();
    }
}
