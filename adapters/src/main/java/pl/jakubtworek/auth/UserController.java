package pl.jakubtworek.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;

import java.net.URI;

@RestController
@RequestMapping("/users")
class UserController {
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);
    private final UserFacade userFacade;

    UserController(final UserFacade userFacade) {
        this.userFacade = userFacade;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) {
        logger.info("Received a registration request for user: {}", registerRequest.username());
        final var result = userFacade.register(registerRequest);
        logger.info("User {} registered successfully.", result.getUsername());
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> register(@RequestBody LoginRequest loginRequest) {
        logger.info("Received a login request for user: {}", loginRequest.username());
        final var result = userFacade.login(loginRequest);
        logger.info("User {} logged in successfully.", result.username());
        return ResponseEntity.ok().body(result);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        logger.error("An error occurred: {}", e.getMessage());
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}
