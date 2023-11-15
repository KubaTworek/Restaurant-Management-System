package pl.jakubtworek.menu;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.test.annotation.DirtiesContext;
import pl.jakubtworek.AbstractIT;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MenuItemControllerE2ETest extends AbstractIT {

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuExists() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertMenuItemResponse(responseMenuItem, new BigDecimal("11.99"));
        assertMenuItemsCount(responseMenu, "Food", 3);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
    }

    @Test
    @DirtiesContext
    void shouldCreateMenuItem_whenMenuDoesNotExist() {
        // given
        final var request = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Fast-Food");

        // when
        final var responseMenuItem = postMenuItem(request);
        final var responseMenu = getMenus();

        // then
        assertMenuItemResponse(responseMenuItem, new BigDecimal("11.99"));
        assertMenuItemsCount(responseMenu, "Food", 2);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertMenuItemsCount(responseMenu, "Fast-Food", 1);
    }

    @Test
    @DirtiesContext
    void shouldCreateInactiveMenuItem_whenMenuExists() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", new BigDecimal("10.99"), "Food");

        // when
        final var responseMenuItem = postMenuItem(request1);
        final var deleteResponse = deleteMenuItemById(responseMenuItem.getId());
        final var responseMenuItemSecond = postMenuItem(request2);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertMenuItemResponse(responseMenuItem, new BigDecimal("11.99"));
        assertMenuItemResponse(responseMenuItemSecond, new BigDecimal("10.99"));
        assertMenuItemsCount(responseMenu, "Food", 3);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
    }

    @Test
    @DirtiesContext
    void shouldCreateInactiveMenuItem_whenMenuDoesNotExist() {
        // given
        final var request1 = new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food");
        final var request2 = new MenuItemRequest("Cheeseburger", new BigDecimal("10.99"), "Fast-Food");

        // when
        final var responseMenuItem = postMenuItem(request1);
        final var deleteResponse = deleteMenuItemById(responseMenuItem.getId());
        final var responseMenuItemSecond = postMenuItem(request2);
        final var responseMenu = getMenus();

        // then
        assertEquals(HttpStatus.NO_CONTENT, deleteResponse.getStatusCode());
        assertMenuItemResponse(responseMenuItem, new BigDecimal("11.99"));
        assertMenuItemResponse(responseMenuItemSecond, new BigDecimal("10.99"));
        assertMenuItemsCount(responseMenu, "Food", 2);
        assertMenuItemsCount(responseMenu, "Drinks", 2);
        assertMenuItemsCount(responseMenu, "Fast-Food", 1);
    }

    @Test
    @DirtiesContext
    void shouldDeleteMenuItemById() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food"));

        // when
        final var firstDelete = deleteMenuItemById(created.getId());
        final var secondDelete = deleteMenuItemById(created.getId());

        // then
        assertEquals(HttpStatus.NO_CONTENT, firstDelete.getStatusCode());
        assertEquals(HttpStatus.NOT_FOUND, secondDelete.getStatusCode());
        final var deletedOrder = getMenuItemById(created.getId());
        assertEquals(Status.INACTIVE, deletedOrder.getStatus());
    }

    @Test
    @DirtiesContext
    void shouldGetMenus() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food"));

        // when
        deleteMenuItemById(created.getId());
        final var response = getMenus();

        // then
        assertMenuItemsCount(response, "Food", 2);
        assertMenuItemsCount(response, "Drinks", 2);
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemById() {
        // given
        final var created = postMenuItem(new MenuItemRequest("Cheeseburger", new BigDecimal("11.99"), "Food"));

        // when
        final var response = getMenuItemById(created.getId());

        // then
        assertMenuItemResponse(response, new BigDecimal("11.99"));
    }

    @Test
    @DirtiesContext
    void shouldGetMenuItemByMenuName() {
        // when
        final var response = getMenuItemByMenuName("Food");

        // then
        assertEquals(2, response.size());
    }

    private void assertMenuItemResponse(MenuItemDto response, BigDecimal expectedPrice) {
        assertEquals("Cheeseburger", response.getName());
        assertEquals(expectedPrice, response.getPrice());
        assertEquals(Status.ACTIVE, response.getStatus());
    }

    private void assertMenuItemsCount(List<MenuDto> responseMenus, String menuName, int expectedItemCount) {
        assertEquals(expectedItemCount, responseMenus.stream().filter(menu -> menuName.equals(menu.getName())).findFirst().get().getMenuItems().size());
    }
}
