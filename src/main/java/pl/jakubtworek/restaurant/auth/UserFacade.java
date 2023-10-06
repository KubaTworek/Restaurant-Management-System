package pl.jakubtworek.restaurant.auth;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.auth.query.SimpleUserQueryDto;

import java.time.Instant;

@Service
public class UserFacade {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;

    UserFacade(final UserRepository userRepository, final UserQueryRepository userQueryRepository) {
        this.userRepository = userRepository;
        this.userQueryRepository = userQueryRepository;
    }

    public SimpleUserQueryDto getUser(String jwt) {
        Claims claims = JwtService.parseJwtClaims(jwt);
        String username = claims.get("username", String.class);

        return userQueryRepository.findDtoByUsername(username).
                orElseThrow(() -> new IllegalStateException("No user registered with this username!"));
    }

    UserDto register(RegisterRequest registerRequest) {
        User user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());

        validateUserWithThatUsernameDoesNotExist(registerRequest.getUsername());

        return userRepository.saveAndReturnDto(user);
    }

    LoginResponse login(LoginRequest loginRequest) {

        User user = userQueryRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalStateException("No user registered with this username!"));
        validPasswords(loginRequest.getPassword(), user.getPassword());

        long expirationDate = Instant.now().toEpochMilli() + 180000;
        String token = JwtService.buildJwt(user.getUsername(), expirationDate);

        return new LoginResponse(
                user.getUsername(),
                token,
                expirationDate
        );
    }

    private void validateUserWithThatUsernameDoesNotExist(String username) {
        if (userQueryRepository.existsByUsername(username)) {
            throw new IllegalStateException("User with this username already exists!");
        }
    }

    private void validPasswords(String passwordProvided, String passwordRegistered) {
        if (!passwordProvided.equals(passwordRegistered)) {
            throw new IllegalStateException("Invalid password!");
        }
    }
}
