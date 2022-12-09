package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuItemControllerTest {
    @Mock
    private MenuItemService menuItemService;

    private MenuItemController menuItemController;

    @BeforeEach
    void setup() {
        menuItemService = mock(MenuItemService.class);

        menuItemController = new MenuItemController(
                menuItemService
        );
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(eq(1L))).thenReturn(expectedMenuItem);

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
        MenuItemDTO expectedMenuItem = createChickenMenuItem().convertEntityToDTO();

        // when
        when(menuItemService.save(menuItemRequest)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemResponse = menuItemController.saveMenuItem(menuItemRequest).getBody();

        // then
        assertEquals("Chicken", menuItemResponse.getName());
        assertEquals(10.99, menuItemResponse.getPrice());
    }


    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(eq(1L))).thenReturn(expectedMenuItem);

        String response = menuItemController.deleteMenuItem(1L).getBody();

        // then
        assertEquals("Menu item with id: 1 was deleted", response);
    }
}
