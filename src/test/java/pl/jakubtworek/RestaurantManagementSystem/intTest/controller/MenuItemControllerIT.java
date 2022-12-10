package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

@SpringBootTest
class MenuItemControllerIT {

    @Autowired
    private MenuItemController menuItemController;

    @MockBean
    private MenuItemRepository menuItemRepository;
    @MockBean
    private MenuRepository menuRepository;

    @Test
    void shouldReturnCreatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItemRequest = createChickenMenuItemRequest();
        MenuItem menuItemCreated = createChickenMenuItem();
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findByName("Food")).thenReturn(expectedMenu);
        when(menuItemRepository.save(any())).thenReturn(menuItemCreated);

        MenuItemResponse menuItemReturned = menuItemController.saveMenuItem(menuItemRequest).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenuItem);

        String response = menuItemController.deleteMenuItem(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Menu item with id: 46e3e96a-7847-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemReturned = menuItemController.getMenuItemById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnMenuItemByMenu() throws Exception {
        // given
        List<MenuItem> expectedMenuItems = createMenuItemListForFood();
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuItemRepository.findByMenu(any())).thenReturn(expectedMenuItems);
        when(menuRepository.findByName("Food")).thenReturn(expectedMenu);

        List<MenuItemResponse> menuItemsReturned = menuItemController.getMenuItemsByMenu("Food").getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItemsInMenu(menuItemsReturned);
    }

    @Test
    void shouldThrowException_whenMenuItemNotExist() {
        // when
        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> menuItemController.getMenuItemById(UUID.fromString("b41874c8-784d-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no menu item in restaurant with that id: b41874c8-784d-11ed-a1eb-0242ac120002", exception.getMessage());
    }
}
