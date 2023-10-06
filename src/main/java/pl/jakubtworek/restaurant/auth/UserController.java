package pl.jakubtworek.restaurant.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

@RestController
@RequestMapping("/users")
class UserController {
    private final UserService userService;

    UserController(final UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    ResponseEntity<UserDto> register(@RequestBody RegisterRequest registerRequest) {
        UserDto result = userService.register(registerRequest);
        return ResponseEntity.created(URI.create("/" + result.getId())).body(result);
    }

    @PostMapping("/login")
    ResponseEntity<LoginResponse> register(@RequestBody LoginRequest loginRequest) {
        LoginResponse result = userService.login(loginRequest);
        return ResponseEntity.ok().body(result);
    }

    @ExceptionHandler(IllegalStateException.class)
    ResponseEntity<String> handleClientError(IllegalStateException e) {
        return ResponseEntity.badRequest().body(e.getMessage());
    }
}