package pl.jakubtworek.auth;

import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;

import java.time.Instant;

public class UserFacade {
    private static final String USER_NOT_FOUND_ERROR = "No user registered with this username!";
    private static final String USER_ALREADY_EXISTS_ERROR = "User with this username already exists!";
    private static final String INVALID_PASSWORD_ERROR = "Invalid password!";
    private static final Long TOKEN_EXPIRATION_TIME = 180000L;

    private final UserRepository userRepository;
    private final UserQueryRepository userQueryRepository;
    private final JwtService jwtService;

    UserFacade(final UserRepository userRepository,
               final UserQueryRepository userQueryRepository,
               final JwtService jwtService
    ) {
        this.userRepository = userRepository;
        this.userQueryRepository = userQueryRepository;
        this.jwtService = jwtService;
    }

    public UserDto getByToken(String jwt) {
        final var username = getUsernameFromJwt(jwt);

        return userQueryRepository.findDtoByUsername(username).
                orElseThrow(() -> new IllegalStateException(USER_NOT_FOUND_ERROR));
    }

    UserDto register(RegisterRequest registerRequest) {
        validateUsernameDoesNotExist(registerRequest.username());

        final var user = createUserFromRequest(registerRequest);
        return toDto(userRepository.save(user));
    }

    LoginResponse login(LoginRequest loginRequest) {
        final var user = getUserByUsername(loginRequest.username());
        validPasswords(loginRequest.password(), user.getPassword());

        final var expirationDate = Instant.now().toEpochMilli() + TOKEN_EXPIRATION_TIME;
        final var token = jwtService.buildJwt(user.getUsername(), expirationDate);

        return new LoginResponse(
                user.getUsername(),
                token,
                expirationDate
        );
    }

    private String getUsernameFromJwt(String jwt) {
        return jwtService.parseJwtClaims(jwt).get("username", String.class);
    }

    private void validateUsernameDoesNotExist(String username) {
        if (userQueryRepository.existsByUsername(username)) {
            throw new IllegalStateException(USER_ALREADY_EXISTS_ERROR);
        }
    }

    private User createUserFromRequest(RegisterRequest registerRequest) {
        User user = new User();
        user.updateInfo(registerRequest.username(), registerRequest.password());
        return user;
    }

    private UserDto getUserByUsername(String username) {
        return userQueryRepository.findDtoByUsername(username)
                .orElseThrow(() -> new IllegalStateException(USER_NOT_FOUND_ERROR));
    }

    private void validPasswords(String passwordProvided, String passwordRegistered) {
        if (!passwordProvided.equals(passwordRegistered)) {
            throw new IllegalStateException(INVALID_PASSWORD_ERROR);
        }
    }

    private UserDto toDto(User user) {
        final var snap = user.getSnapshot();
        return UserDto.create(snap.getId(), snap.getUsername(), snap.getPassword());
    }
}
