/*
package pl.jakubtworek.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;

import java.time.Instant;
import java.util.HashSet;
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
    void shouldReturnUserByToken() {
        // given
        final var jwt = "sample_jwt";
        final var claims = mock(Claims.class);
        final var expectedUser = new SimpleUser(1L, "john.doe");

        when(jwtService.parseJwtClaims(jwt)).thenReturn(claims);
        when(claims.get("username", String.class)).thenReturn("john.doe");
        when(userQueryRepository.findSimpleByUsername("john.doe")).thenReturn(Optional.of(expectedUser));

        // when
        final SimpleUser result = userFacade.getByToken(jwt);

        // then
        assertEquals(expectedUser, result);
    }

    @Test
    void shouldRegisterUser() {
        // given
        final var request = new RegisterRequest("john.doe", "password123");
        final var expectedUser = createUser(request.getUsername(), request.getPassword());

        when(userRepository.save(any())).thenReturn(expectedUser);

        // when
        final UserDto result = userFacade.register(request);

        // then
        assertUserEquals(expectedUser, result);
    }

    @Test
    void shouldLoginUser() {
        // given
        final var request = new LoginRequest("john.doe", "password123");
        final var expectedUser = UserDto.create(1L, "john.doe", "password123");

        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(expectedUser));
        when(jwtService.buildJwt(eq("john.doe"), any(Long.class))).thenReturn("token");

        // when
        final LoginResponse result = userFacade.login(request);

        // then
        assertEquals(expectedUser.getUsername(), result.getUsername());
        assertEquals("token", result.getToken());
        assertTrue(result.getTokenExpirationDate() > Instant.now().toEpochMilli());
    }

    @Test
    void shouldThrowException_whenRegisterUserWithSameUsername() {
        // given
        final var request = new RegisterRequest("existing_user", "password123");

        when(userQueryRepository.existsByUsername("existing_user")).thenReturn(true);

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.register(request));
    }

    @Test
    void shouldThrowException_whenLoginWithWrongCredentials() {
        // given
        final var request = new LoginRequest("john.doe", "password123");
        final var expectedUser = UserDto.create(1L, "john.doe", "invalid_password");

        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(expectedUser));

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.login(request));
    }

    private User createUser(String username, String password) {
        return User.restore(new UserSnapshot(
                1L, username, password, new HashSet<>()
        ));
    }

    private void assertUserEquals(User expected, UserDto actual) {
        assertEquals(expected.getSnapshot().getId(), actual.getId());
        assertEquals(expected.getSnapshot().getUsername(), actual.getUsername());
        assertEquals(expected.getSnapshot().getPassword(), actual.getPassword());
    }
}
*/
