package pl.jakubtworek.auth;

import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.common.vo.Role;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldRegisterUser() {
        // given
        final var request = new RegisterRequest("user", "password", "USER", "John", "Doe", "john.doe@mail.com", "450642154");

        // when
        final var response = registerUser(request);

        // then
        assertEquals("user", response.getUsername());
        assertEquals("password", response.getPassword());
        assertEquals(Role.USER, response.getRole());
    }

    @Test
    @DirtiesContext
    void shouldLoginUser() {
        // given
        final var request = new LoginRequest("testuser", "password");

        // when
        final var response = loginUser(request);

        // then
        assertEquals("testuser", response.username());
        assertNotNull(response.token());
        assertNotNull(response.tokenExpirationDate());
    }
}
