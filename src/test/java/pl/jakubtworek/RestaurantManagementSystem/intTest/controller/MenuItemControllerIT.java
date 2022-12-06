package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
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
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItem = menuItemController.getMenuItemById(1L).getBody();

        // then
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() {
        // when
        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> menuItemController.getMenuItemById(4L));

        // then
        assertEquals("There are no menu item in restaurant with that id: 4", exception.getMessage());
    }

    @Test
    void shouldReturnCreatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItemRequest = createChickenMenuItemRequest();
        MenuItem menuItemCreated = createChickenMenuItem();
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findByName("Food")).thenReturn(expectedMenu);
        when(menuItemRepository.save(any())).thenReturn(menuItemCreated);

        MenuItemResponse menuItemResponse = menuItemController.saveMenuItem(menuItemRequest).getBody();

        // then
        assertEquals("Chicken", menuItemResponse.getName());
        assertEquals(10.99, menuItemResponse.getPrice());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItem> expectedMenuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemDeleted = menuItemController.deleteMenuItem(1L).getBody();

        // then
        assertEquals("Chicken", menuItemDeleted.getName());
        assertEquals(10.99, menuItemDeleted.getPrice());
    }
}
