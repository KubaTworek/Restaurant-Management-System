package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuControllerE2ETest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    @DirtiesContext
    void testCreateMenu() {
        // given
        MenuRequest request = new MenuRequest("Dinner Menu");

        // when
        ResponseEntity<MenuDto> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                request,
                MenuDto.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void testGetMenus() {
        // given
        MenuRequest request1 = new MenuRequest("Dinner Menu");
        MenuRequest request2 = new MenuRequest("Lunch Menu");
        restTemplate.postForEntity("http://localhost:" + port + "/menu", request1, MenuDto.class);
        restTemplate.postForEntity("http://localhost:" + port + "/menu", request2, MenuDto.class);

        // when
        ResponseEntity<MenuDto[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu",
                MenuDto[].class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MenuDto> menus = Arrays.asList(response.getBody());
        assertEquals(2, menus.size());
    }

    @Test
    @DirtiesContext
    void testDeleteMenu() {
        // Tworzenie menu
        MenuRequest request = new MenuRequest("Dinner Menu");
        ResponseEntity<MenuDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                request,
                MenuDto.class
        );
        Long menuId = createdResponse.getBody().getId();

        // Usunięcie menu
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/menu/" + menuId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Sprawdzenie, czy menu zostało usunięte
        ResponseEntity<MenuDto> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu/" + menuId,
                MenuDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @DirtiesContext
    void testGetMenuById() {
        // given
        MenuRequest request = new MenuRequest("Dinner Menu");
        ResponseEntity<MenuDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                request,
                MenuDto.class
        );

        Long menuId = createdResponse.getBody().getId();

        // when
        ResponseEntity<MenuDto> retrievedResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu/" + menuId,
                MenuDto.class
        );

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Dinner Menu", retrievedResponse.getBody().getName());
    }
}
