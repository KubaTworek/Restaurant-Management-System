package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.auth.dto.UserDto;

import java.time.Instant;

public class UserFacade {
    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final JwtService jwtService;

    UserFacade(final UserRepository userRepository, final UserQueryRepository userQueryRepository, final JwtService jwtService) {
        this.userRepository = userRepository;
        this.userQueryRepository = userQueryRepository;
        this.jwtService = jwtService;
    }

    public SimpleUser getUser(String jwt) {
        final var claims = jwtService.parseJwtClaims(jwt);
        final var username = claims.get("username", String.class);

        return userQueryRepository.findSimpleByUsername(username).
                orElseThrow(() -> new IllegalStateException("No user registered with this username!"));
    }

    UserDto register(RegisterRequest registerRequest) {
        final var user = new User();
        user.setUsername(registerRequest.getUsername());
        user.setPassword(registerRequest.getPassword());

        validateUserWithThatUsernameDoesNotExist(registerRequest.getUsername());

        return toDto(userRepository.save(user));
    }

    LoginResponse login(LoginRequest loginRequest) {

        final var user = userQueryRepository.findDtoByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new IllegalStateException("No user registered with this username!"));
        validPasswords(loginRequest.getPassword(), user.getPassword());

        final var expirationDate = Instant.now().toEpochMilli() + 180000;
        final var token = jwtService.buildJwt(user.getUsername(), expirationDate);

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

    private UserDto toDto(User user) {
        return UserDto.create(user.getId(), user.getUsername(), user.getPassword());
    }
}
