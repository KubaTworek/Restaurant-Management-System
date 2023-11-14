package pl.jakubtworek.auth;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.RegisterRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class UserControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldRegisterUser() {
        // given
        final var request = new RegisterRequest("user", "password");

        // when
        final var response = registerUser(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("user", response.getBody().getUsername());
    }

    @Test
    @DirtiesContext
    void shouldLoginUser() {
        // given
        final var request = new LoginRequest("testuser", "password");

        // when
        final var response = loginUser(request);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", response.getBody().username());
        assertNotNull(response.getBody().token());
        assertNotNull(response.getBody().tokenExpirationDate());
    }
}
