package pl.jakubtworek.RestaurantManagementSystem.security.filter;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import pl.jakubtworek.RestaurantManagementSystem.security.constants.SecurityConstants;

import javax.crypto.SecretKey;
import javax.servlet.*;
import javax.servlet.http.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class JWTTokenGeneratorFilter extends OncePerRequestFilter {

    @Override
    public void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (null != authentication) {
            SecretKey key = Keys.hmacShaKeyFor(SecurityConstants.JWT_KEY.getBytes(StandardCharsets.UTF_8));
            String jwt = Jwts.builder().setIssuer("Restaurant").setSubject("JWT Token")
                    .claim("username", authentication.getName())
                    .claim("authorities", populateAuthorities(authentication.getAuthorities()))
                    .setIssuedAt(new Date())
                    .setExpiration(new Date((new Date()).getTime() + 30000000))
                    // user jwt = eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJ1c2VyIiwiYXV0aG9yaXRpZXMiOiJST0xFX1VTRVIiLCJpYXQiOjE2NzA5OTA3MTEsImV4cCI6MTAwMTY3MDk5MDcxMX0.FQXWI59l0axX413c5H4F1lByE2mSK4fe4pBvvR-gYV8
                    // admin jwt = eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJSZXN0YXVyYW50Iiwic3ViIjoiSldUIFRva2VuIiwidXNlcm5hbWUiOiJhZG1pbiIsImF1dGhvcml0aWVzIjoiUk9MRV9BRE1JTiIsImlhdCI6MTY3MDk5MDgwMSwiZXhwIjoxMDAxNjcwOTkwODAxfQ.-3BdaIuq5rSWzf9zIdkR3S1ftAk9SSGY5iaX0zCYO08
                    .signWith(key).compact();
            response.addHeader(SecurityConstants.JWT_HEADER, jwt);
        }

        chain.doFilter(request, response);
    }

/*    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return !request.getServletPath().equals("/user");
    }*/

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        Set<String> authoritiesSet = new HashSet<>();
        for (GrantedAuthority authority : collection) {
            authoritiesSet.add(authority.getAuthority());
        }
        return String.join(",", authoritiesSet);
    }
}
