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
        when(menuItemRepository.findById(1L)).thenReturn(expectedMenuItem);

        String response = menuItemController.deleteMenuItem(1L).getBody();

        // then
        assertEquals("Menu item with id: 1 was deleted", response);
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemReturned = menuItemController.getMenuItemById(1L).getBody();

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
        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> menuItemController.getMenuItemById(4L));

        // then
        assertEquals("There are no menu item in restaurant with that id: 4", exception.getMessage());
    }
}
