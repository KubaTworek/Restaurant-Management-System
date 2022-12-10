package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
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
    void shouldReturnCreatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItemRequest = createChickenMenuItemRequest();
        MenuItemDTO expectedMenuItem = createChickenMenuItem().convertEntityToDTO();

        // when
        when(menuItemService.save(menuItemRequest)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemCreated = menuItemController.saveMenuItem(menuItemRequest).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemCreated);
    }


    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(1L)).thenReturn(expectedMenuItem);

        String response = menuItemController.deleteMenuItem(1L).getBody();

        // then
        assertEquals("Menu item with id: 1 was deleted", response);
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(1L)).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemReturned = menuItemController.getMenuItemById(1L).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() {
        // when
        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> menuItemController.getMenuItemById(4L));

        // then
        assertEquals("There are no menu item in restaurant with that id: 4", exception.getMessage());
    }

    @Test
    void shouldReturnMenuItemByMenu() throws Exception {
        // given
        List<MenuItemDTO> expectedMenuItems = createMenuItemListForFood()
                .stream()
                .map(MenuItem::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(menuItemService.findByMenu(anyString())).thenReturn(expectedMenuItems);

        List<MenuItemResponse> menuItemsReturned = menuItemController.getMenuItemsByMenu("Food").getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItems(menuItemsReturned);
    }
}
