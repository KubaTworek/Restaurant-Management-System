package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.order.dto.Status;

import java.math.BigDecimal;
import java.util.Arrays;
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

    @Test
    @DirtiesContext
    void shouldCreateInactiveMenuItem_whenMenuIsExist1() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var deleteResponse = deleteMenuItemById(responseMenuItem.getBody().getId());
        final var responseMenuItemSecond = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.CREATED, responseMenuItem.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertNotNull(responseMenuItem.getBody().getId());
        assertEquals(responseMenuItem.getBody().getId(), responseMenuItemSecond.getBody().getId());
        assertEquals(2, responseMenu.getBody().length);
        assertEquals(3, Arrays.stream(responseMenu.getBody()).sequential().filter(menu -> "Food".equals(menu.getName())).findFirst().get().getMenuItems().size());
    }

    @Test
    @DirtiesContext
    void shouldCreateInactiveMenuItem_whenMenuIsNotExist() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", new BigDecimal("10.99"), "Fast-Food");

        // when
        final var responseMenuItem = postMenuItem(request1);
        final var deleteResponse = deleteMenuItemById(responseMenuItem.getBody().getId());
        final var responseMenuItemSecond = postMenuItem(request2);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.CREATED, responseMenuItem.getStatusCode());
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertNotNull(responseMenuItem.getBody().getId());
        assertEquals(responseMenuItem.getBody().getId(), responseMenuItemSecond.getBody().getId());
        assertEquals(3, responseMenu.getBody().length);
        assertEquals(1, Arrays.stream(responseMenu.getBody()).sequential().filter(menu -> "Fast-Food".equals(menu.getName())).findFirst().get().getMenuItems().size());
        assertEquals(new BigDecimal("10.99"), Arrays.stream(responseMenu.getBody()).sequential().filter(menu -> "Fast-Food".equals(menu.getName())).findFirst().get().getMenuItems().get(0).getPrice());
    }

    @Test
    @DirtiesContext
    void shouldDeleteMenuItemById() {
        // given
        final var createdId = postMenuItem(
                new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food")
        ).getBody().getId();

        // when
        final var firstDelete = deleteMenuItemById(createdId);
        final var secondDelete = deleteMenuItemById(createdId);

        // then
        assertEquals(HttpStatus.NO_CONTENT, firstDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, secondDelete.getStatusCode());
        final var deletedOrder = getMenuItemById(createdId);
        assertEquals(Status.INACTIVE, deletedOrder.getBody().getStatus());
    }

    @Test
    @DirtiesContext
    void shouldGetMenus() {
        // given
        final var createdId = postMenuItem(
                new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food")
        ).getBody().getId();

        // when
        deleteMenuItemById(createdId);
        final var response = getMenus();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        final var menus = List.of(response.getBody());
        assertEquals(2, menus.size());
        assertEquals(2, menus.get(0).getMenuItems().size());
        assertEquals(2, menus.get(1).getMenuItems().size());
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
