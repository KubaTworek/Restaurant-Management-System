package pl.jakubtworek.menu;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class MenuItemControllerE2ETest {
    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private JdbcTemplate jdbcTemplate;


    @Test
    @DirtiesContext
    void testCreateMenuItem() {
        // given
        restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                new MenuRequest("Lunch"),
                MenuDto.class
        );
        MenuItemRequest request = new MenuItemRequest("Burger", 1099, "Lunch");

        // when
        ResponseEntity<MenuItemDto> response = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu-items",
                request,
                MenuItemDto.class
        );

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void testDeleteMenuItem() {
        restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                new MenuRequest("Lunch"),
                MenuDto.class
        );
        // Tworzenie elementu menu
        MenuItemRequest request = new MenuItemRequest("Burger", 1099, "Lunch");
        ResponseEntity<MenuItemDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu-items",
                request,
                MenuItemDto.class
        );
        Long menuItemId = createdResponse.getBody().getId();

        // Usunięcie elementu menu
        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());

        // Sprawdzenie, czy element menu został usunięty
        ResponseEntity<MenuItemDto> getResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                MenuItemDto.class
        );

        assertEquals(HttpStatus.NOT_FOUND, getResponse.getStatusCode());
    }

    @Test
    @DirtiesContext
    void testGetMenuItemById() {
        // given
        restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                new MenuRequest("Lunch"),
                MenuDto.class
        );
        MenuItemRequest request = new MenuItemRequest("Burger", 1099, "Lunch");
        ResponseEntity<MenuItemDto> createdResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/menu-items",
                request,
                MenuItemDto.class
        );

        Long menuItemId = createdResponse.getBody().getId();

        // when
        ResponseEntity<MenuItemDto> retrievedResponse = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/" + menuItemId,
                MenuItemDto.class
        );

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Burger", retrievedResponse.getBody().getName());
        assertEquals(1099, retrievedResponse.getBody().getPrice());
    }

    @Test
    @DirtiesContext
    void testGetMenuItemsByMenu() {
        // given
        restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                new MenuRequest("Lunch"),
                MenuDto.class
        );
        restTemplate.postForEntity(
                "http://localhost:" + port + "/menu",
                new MenuRequest("Dinner"),
                MenuDto.class
        );
        MenuItemRequest request1 = new MenuItemRequest("Burger", 1099, "Lunch");
        MenuItemRequest request2 = new MenuItemRequest("Pizza", 1299, "Dinner");
        restTemplate.postForEntity("http://localhost:" + port + "/menu-items", request1, MenuItemDto.class);
        restTemplate.postForEntity("http://localhost:" + port + "/menu-items", request2, MenuItemDto.class);

        // when
        ResponseEntity<MenuItemDto[]> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/menu-items/menu/Lunch",
                MenuItemDto[].class
        );

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        List<MenuItemDto> menuItems = Arrays.asList(response.getBody());
        assertEquals(1, menuItems.size());
        assertEquals("Burger", menuItems.get(0).getName());
        assertEquals(1099, menuItems.get(0).getPrice());
    }

    @AfterEach
    void clean() {
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders__employee");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders__menu_items");
        jdbcTemplate.execute("DROP TABLE IF EXISTS orders");
        jdbcTemplate.execute("DROP TABLE IF EXISTS employees");
        jdbcTemplate.execute("DROP TABLE IF EXISTS menu_items");
        jdbcTemplate.execute("DROP TABLE IF EXISTS menu");
        jdbcTemplate.execute("DROP TABLE IF EXISTS users");
    }
}
