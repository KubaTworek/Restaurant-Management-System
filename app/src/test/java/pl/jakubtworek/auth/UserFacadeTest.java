package pl.jakubtworek.auth;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.common.vo.Role;

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
        MockitoAnnotations.openMocks(this);
        userFacade = new UserFacade(
                userRepository,
                userQueryRepository,
                jwtService
        );
    }

    @Test
    void shouldReturnUserByToken() {
        // given
        final var jwt = "sample_jwt";
        final var claims = mock(Claims.class);
        final var expectedUser = UserDto.create(1L, "john.doe", "password", Role.USER);

        when(jwtService.parseJwtClaims(jwt)).thenReturn(claims);
        when(claims.get("username", String.class)).thenReturn("john.doe");
        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(expectedUser));

        // when
        final var result = userFacade.getByToken(jwt);

        // then
        assertEquals(expectedUser, result);
    }

    @Test
    void shouldRegisterUser() {
        // given
        final var request = new RegisterRequest("john.doe", "password123", "USER", "John", "Doe", "john.doe@mail.com", "450642154");
        final var expectedUser = createUser(request.username(), request.password(), request.role(), request.firstName(), request.lastName(), request.email(), request.phone());

        when(userRepository.save(any())).thenReturn(expectedUser);

        // when
        final var result = userFacade.register(request);

        // then
        assertUserEquals(expectedUser, result);
    }

    @Test
    void shouldLoginUser() {
        // given
        final var request = new LoginRequest("john.doe", "password123");
        final var expectedUser = UserDto.create(1L, "john.doe", "password123", Role.USER);

        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(expectedUser));
        when(jwtService.buildJwt(eq("john.doe"), any(Long.class))).thenReturn("token");

        // when
        final var result = userFacade.login(request);

        // then
        assertEquals(expectedUser.getUsername(), result.username());
        assertEquals("token", result.token());
        assertTrue(result.tokenExpirationDate() > Instant.now().toEpochMilli());
    }

    @Test
    void shouldThrowException_whenRegisterUserWithSameUsername() {
        // given
        final var request = new RegisterRequest("existing_user", "password123", "USER", "John", "Doe", "john.doe@mail.com", "450642154");

        when(userQueryRepository.existsByUsername("existing_user")).thenReturn(true);

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.register(request));
    }

    @Test
    void shouldThrowException_whenLoginWithWrongCredentials() {
        // given
        final var request = new LoginRequest("john.doe", "password123");
        final var expectedUser = UserDto.create(1L, "john.doe", "invalid_password", Role.USER);

        when(userQueryRepository.findDtoByUsername("john.doe")).thenReturn(Optional.of(expectedUser));

        // when & then
        assertThrows(IllegalStateException.class, () -> userFacade.login(request));
    }

    private User createUser(String username, String password, String role, String firstName, String lastName, String email, String phone) {
        return User.restore(new UserSnapshot(
                1L, username, password, Role.valueOf(role), firstName, lastName, email, phone
        ));
    }

    private void assertUserEquals(User expected, UserDto actual) {
        assertEquals(expected.getSnapshot().getId(), actual.getId());
        assertEquals(expected.getSnapshot().getUsername(), actual.getUsername());
        assertEquals(expected.getSnapshot().getPassword(), actual.getPassword());
    }
}
