package pl.jakubtworek.auth;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.jakubtworek.auth.dto.LoginRequest;
import pl.jakubtworek.auth.dto.LoginResponse;
import pl.jakubtworek.auth.dto.RegisterRequest;
import pl.jakubtworek.auth.dto.UserDto;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class UserControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void testRegisterUser() {
        // given
        RegisterRequest request = new RegisterRequest("testuser", "password");

        // when
        ResponseEntity<UserDto> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/register",
                request,
                UserDto.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
        assertEquals("testuser", response.getBody().getUsername());
    }

    @Test
    @DirtiesContext
    void testLoginUser() {
        // given
        RegisterRequest registerRequest = new RegisterRequest("testuser", "password");
        restTemplate.postForEntity("http://localhost:" + port + "/users/register", registerRequest, UserDto.class);

        LoginRequest loginRequest = new LoginRequest("testuser", "password");

        // when
        ResponseEntity<LoginResponse> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/users/login",
                loginRequest,
                LoginResponse.class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("testuser", response.getBody().getUsername());
        assertNotNull(response.getBody().getToken());
        assertNotNull(response.getBody().getTokenExpirationDate());
    }
}

