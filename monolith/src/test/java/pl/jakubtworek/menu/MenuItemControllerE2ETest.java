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

class MenuItemControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenuItem() {
        // given
        postMenu(new MenuRequest("Lunch"));

        final var request = new MenuItemRequest("Burger", 1099, "Lunch");

        // when
        final var response = postMenuItem(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

    @Test
    @DirtiesContext
    void shouldDeleteMenuItemById() {
        // given
        postMenu(new MenuRequest("Lunch"));
        final var createdId = postMenuItem(
                new MenuItemRequest("Burger", 1099, "Lunch")
        ).getBody().getId();

        // when
        final var response = deleteMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemById() {
        // given
        postMenu(new MenuRequest("Lunch"));
        final var createdId = postMenuItem(
                new MenuItemRequest("Burger", 1099, "Lunch")
        ).getBody().getId();

        // when
        final var retrievedResponse = getMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Burger", retrievedResponse.getBody().getName());
        assertEquals(1099, retrievedResponse.getBody().getPrice());
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemByMenuName() {
        // given
        postMenu(new MenuRequest("Lunch"));
        postMenuItem(new MenuItemRequest("Burger", 1099, "Lunch"));

        postMenu(new MenuRequest("Dinner"));
        postMenuItem(new MenuItemRequest("Pizza", 1299, "Dinner"));

        // when
        final var response = getMenuItemByMenuName("Lunch");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var menuItems = List.of(response.getBody());
        assertEquals(1, menuItems.size());
        assertEquals("Burger", menuItems.get(0).getName());
        assertEquals(1099, menuItems.get(0).getPrice());
    }
}
