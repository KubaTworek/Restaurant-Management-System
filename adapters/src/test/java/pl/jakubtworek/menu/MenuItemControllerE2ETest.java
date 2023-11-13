package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class MenuItemControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuIsExist() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.CREATED, responseMenuItem.getStatusCode());
        assertNotNull(responseMenuItem.getBody().getId());
        assertEquals(2, responseMenu.getBody().length);
    }

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuIsNotExist() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Fast-Foods");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.CREATED, responseMenuItem.getStatusCode());
        assertNotNull(responseMenuItem.getBody().getId());
        assertEquals(3, responseMenu.getBody().length);
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
    void shouldGetMenuItemById() {
        // given
        final var createdId = postMenuItem(
                new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food")
        ).getBody().getId();

        // when
        final var retrievedResponse = getMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.OK, retrievedResponse.getStatusCode());
        assertEquals("Cheeseburger", retrievedResponse.getBody().getName());
        assertEquals(new BigDecimal("11.99"), retrievedResponse.getBody().getPrice());
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
        assertEquals(new BigDecimal("10.99"), menuItems.get(0).getPrice());
    }
}
