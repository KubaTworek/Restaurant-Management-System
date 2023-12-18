package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.web.client.RestClientException;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class MenuItemControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuExists() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertMenuItemResponse(responseMenuItem, BigDecimal.valueOf(11.99));
        assertMenuItemsCount(responseMenu, "Food", 3);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertUpdatedMenuItem(responseMenu, "Food", BigDecimal.valueOf(11.99));

    }

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuDoesNotExist() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Fast-Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertMenuItemResponse(responseMenuItem, BigDecimal.valueOf(11.99));
        assertMenuItemsCount(responseMenu, "Food", 2);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertMenuItemsCount(responseMenu, "Fast-Food", 1);
        assertUpdatedMenuItem(responseMenu, "Fast-Food", BigDecimal.valueOf(11.99));

    }

    @Test
    @DirtiesContext
    void shouldUpdateInactiveMenuItem_whenMenuExists() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(10.99), "Food");

        // when
        final var responseMenuItem = postMenuItem(request1);
        final var deleteResponse = deactivateMenuItemById(responseMenuItem.getId());
        final var responseMenuItemSecond = postMenuItem(request2);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertMenuItemResponse(responseMenuItem, BigDecimal.valueOf(11.99));
        assertMenuItemResponse(responseMenuItemSecond, BigDecimal.valueOf(10.99));
        assertMenuItemsCount(responseMenu, "Food", 3);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertUpdatedMenuItem(responseMenu, "Food", BigDecimal.valueOf(10.99));
    }

    @Test
    @DirtiesContext
    void shouldUpdateInactiveMenuItem_whenMenuDoesNotExist() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(10.99), "Fast-Food");

        // when
        final var responseMenuItem = postMenuItem(request1);
        final var deleteResponse = deactivateMenuItemById(responseMenuItem.getId());
        final var responseMenuItemSecond = postMenuItem(request2);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertMenuItemResponse(responseMenuItem, BigDecimal.valueOf(11.99));
        assertMenuItemResponse(responseMenuItemSecond, BigDecimal.valueOf(10.99));
        assertMenuItemsCount(responseMenu, "Food", 2);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertMenuItemsCount(responseMenu, "Fast-Food", 1);
        assertUpdatedMenuItem(responseMenu, "Fast-Food", BigDecimal.valueOf(10.99));

    }

    @Test
    @DirtiesContext
    void shouldNotUpdateMenuItem_whenIsActive() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(10.99), "Food");

        postMenuItem(request1);

        // when & then
        assertThrows(RestClientException.class, () ->
                postMenuItem(request2)
        );
    }

    @Test
    @DirtiesContext
    void shouldDeleteMenuItemById() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food"));

        // when
        final var deleteResponse = deleteMenuItemById(created.getId());
        final var response = getMenus();

        // then
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertEquals(2, response.stream().filter(menu -> "Food".equals(menu.getName())).findFirst().get().getMenuItems().size());
    }

    @Test
    @DirtiesContext
    void shouldDeactivateMenuItemById() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food"));

        // when
        final var firstDelete = deactivateMenuItemById(created.getId());
        final var secondDelete = deactivateMenuItemById(created.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT, firstDelete.getStatusCode());
        assertEquals(HttpStatus.BAD_REQUEST, secondDelete.getStatusCode());
        assertMenuItemHasInactiveStatus(created.getId());
    }

    @Test
    @DirtiesContext
    void shouldGetMenus() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food"));

        // when
        deactivateMenuItemById(created.getId());
        final var response = getMenus();

        // then
        assertMenuItemsCount(response, "Food", 2);
        assertMenuItemsCount(response, "Drinks", 2);
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemById() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", BigDecimal.valueOf(11.99), "Food"));

        // when
        final var response = getMenuItemById(created.getId());

        // then
        assertMenuItemResponse(response, BigDecimal.valueOf(11.99));
    }

    private void assertMenuItemResponse(MenuItemDto response, BigDecimal expectedPrice) {
        assertEquals("Cheeseburger", response.getName());
        assertEquals(expectedPrice, response.getPrice());
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    private void assertUpdatedMenuItem(List<MenuDto> responseMenus, String menuName, BigDecimal expectedPrice) {
        final var updatedMenuItem = responseMenus.stream().filter(m -> menuName.equals(m.getName())).findFirst().get().getMenuItems().stream().filter(mi -> "Cheeseburger".equals(mi.getName())).findFirst().get();
        assertEquals("Cheeseburger", updatedMenuItem.getName());
        assertEquals(expectedPrice, updatedMenuItem.getPrice());
        assertEquals(Status.ACTIVE, updatedMenuItem.getStatus());
    }

    private void assertMenuItemsCount(List<MenuDto> responseMenus, String menuName, int expectedItemCount) {
        assertEquals(expectedItemCount, responseMenus.stream().filter(menu -> menuName.equals(menu.getName())).findFirst().get().getMenuItems().size());
    }

    private void assertMenuItemHasInactiveStatus(Long itemId) {
        final var deletedOrder = getMenuItemById(itemId);
        assertEquals(Status.INACTIVE, deletedOrder.getStatus());
    }
}
