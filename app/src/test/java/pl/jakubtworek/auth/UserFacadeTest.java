package pl.jakubtworek.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.auth.dto.UserDto;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class UserFacadeTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private UserQueryRepository userQueryRepository;
    @Mock
    private JwtService jwtService;

    private UserFacade userFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        userFacade = new UserFacade(userRepository, userQueryRepository, jwtService);
    }

    @Test
    void testGetUser() {
        // given
        final var jwt = "sample_jwt";
        final var claims = mock(Claims.class);
        when(jwtService.parseJwtClaims(jwt)).thenReturn(claims);
        when(claims.get("username", String.class)).thenReturn("john.doe");

        final var simpleUser = new SimpleUser(1L, "john.doe");
        when(userQueryRepository.findSimpleByUsername("john.doe")).thenReturn(Optional.of(simpleUser));

        // when
        final SimpleUser result = userFacade.getUser(jwt);

        // then
        assertEquals(simpleUser, result);
    }

    @Test
    void testRegister() {
        // given
        final var registerRequest = new RegisterRequest("john.doe", "password123");
        final var user = createUser(registerRequest.getUsername(), registerRequest.getPassword());
        when(userRepository.save(any())).thenReturn(user);

        // when
        final UserDto result = userFacade.register(registerRequest);

        // then
        assertEquals(user.getId(), result.getId());
        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
    }

    @Test
    void testLogin() {
        // given
        final var loginRequest = new LoginRequest("john.doe", "password123");
        final var userDto = UserDto.create(1L, "john.doe", "password123");
        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(userDto));
        when(jwtService.buildJwt(eq("john.doe"), any(Long.class))).thenReturn("token");

        // when
        final LoginResponse result = userFacade.login(loginRequest);

        // then
        assertEquals("john.doe", result.getUsername());
        assertEquals("token", result.getToken());
        assertTrue(result.getTokenExpirationDate() > Instant.now().toEpochMilli());
    }

    @Test
    void testRegisterExistingUser() {
        // given
        final var registerRequest = new RegisterRequest("existing_user", "password123");
        when(userQueryRepository.existsByUsername("existing_user")).thenReturn(true);

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.register(registerRequest));
    }

    @Test
    void testLoginInvalidPassword() {
        // given
        final var loginRequest = new LoginRequest("john.doe", "password123");
        final var userDto = UserDto.create(1L, "john.doe", "invalid_password");
        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(userDto));

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.login(loginRequest));
    }

    private User createUser(String username, String password) {
        final var user = new User();
        user.setUsername(username);
        user.setPassword(password);
        return user;
    }
}
