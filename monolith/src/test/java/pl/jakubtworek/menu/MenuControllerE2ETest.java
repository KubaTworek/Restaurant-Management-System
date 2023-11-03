package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenu() {
        // given
        final var request = new MenuRequest("Dinner Menu");

        // when
        final var response = postMenu(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void shouldGetMenus() {
        // given
        postMenu(new MenuRequest("Dinner Menu"));
        postMenu(new MenuRequest("Lunch Menu"));

        // when
        final var response = getMenus();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var menus = List.of(response.getBody());
        assertEquals(2, menus.size());
    }

    @Test
    @DirtiesContext
    void shouldDeleteMenuById() {
        // given
        final var createdId = postMenu(
                new MenuRequest("Dinner Menu")
        ).getBody().getId();

        // when
        final var response = deleteMenuById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void shouldGetMenuById() {
        // given
        final var createdId = postMenu(
                new MenuRequest("Dinner Menu")
        ).getBody().getId();

        // when
        final var retrievedResponse = getMenuById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Dinner Menu", retrievedResponse.getBody().getName());
    }
}
