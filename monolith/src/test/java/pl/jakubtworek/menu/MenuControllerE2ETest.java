package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenu() {
        // given
        final var request = new MenuRequest("Lunch");

        // when
        final var response = postMenu(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void shouldGetMenus() {
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
                new MenuRequest("Lunch")
        ).getBody().getId();
        final var createdMenuItemId = postMenuItem(
                new MenuItemRequest("Burger", 1099, "Lunch")
        ).getBody().getId();

        // when
        final var response = deleteMenuById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        final var menuItemResponse = getMenuItemById(createdMenuItemId);
        assertEquals(HttpStatus.NOT_FOUND, menuItemResponse.getStatusCode());
    }

    @Test
    @DirtiesContext
    void shouldGetMenuById() {
        // given
        final var createdId = postMenu(
                new MenuRequest("Lunch")
        ).getBody().getId();

        // when
        final var retrievedResponse = getMenuById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Lunch", retrievedResponse.getBody().getName());
    }
}
