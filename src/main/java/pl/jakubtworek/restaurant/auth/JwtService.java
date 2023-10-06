package pl.jakubtworek.restaurant.auth;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;

class JwtService {

    public static String buildJwt(String username, long expirationDate) {
        return Jwts.builder()
                .setIssuer("Social Media")
                .setSubject("JWT Token")
                .claim("username", username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(expirationDate))
                .signWith(createSecretKey())
                .compact();
    }

    public static Claims parseJwtClaims(String jwt) {
        String jwtWithoutBearer = jwt.replaceFirst("Bearer ", "");
        return Jwts.parserBuilder()
                .setSigningKey(createSecretKey())
                .build()
                .parseClaimsJws(jwtWithoutBearer)
                .getBody();
    }

    private static SecretKey createSecretKey() {
        byte[] keyBytes = SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
