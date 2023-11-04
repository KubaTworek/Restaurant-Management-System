package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuItemControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenuItem() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", 1199, "Food");

        // when
        final var response = postMenuItem(request);

        // then
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody().getId());
    }

/*    @Test
    @DirtiesContext
    void shouldDeleteMenuItemById() {
        // given
        final var createdId = postMenuItem(
                new MenuItemRequest("Cheeseburger", 1199, "Food")
        ).getBody().getId();

        final var request = new OrderRequest(
                "ON_SITE",
                List.of("Cheeseburger")
        );
        final var createdOrderId = postOrder(request, userToken).getBody().getId();

        // when
        final var response = deleteMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        final var orderResponse = getOrderById(createdOrderId, userToken);
        assertEquals(HttpStatus.OK, orderResponse.getStatusCode());
    }*/

    @Test
    @DirtiesContext
    void shouldGetMenuItemById() {
        // given
        final var createdId = postMenuItem(
                new MenuItemRequest("Cheeseburger", 1199, "Food")
        ).getBody().getId();

        // when
        final var retrievedResponse = getMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Cheeseburger", retrievedResponse.getBody().getName());
        assertEquals(1199, retrievedResponse.getBody().getPrice());
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemByMenuName() {
        // when
        final var response = getMenuItemByMenuName("Food");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var menuItems = List.of(response.getBody());
        assertEquals(2, menuItems.size());
        assertEquals("Burger", menuItems.get(0).getName());
        assertEquals(1099, menuItems.get(0).getPrice());
    }
}
