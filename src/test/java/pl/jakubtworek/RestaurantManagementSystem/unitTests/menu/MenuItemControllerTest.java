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
    void shouldReturnUpdatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItemRequest = createChickenMenuItemRequest();
        MenuItemDTO expectedMenuItem = createChickenMenuItem().convertEntityToDTO();

        // when
        when(menuItemService.update(any(), any())).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemCreated = menuItemController.updateMenuItem(menuItemRequest, UUID.randomUUID()).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemCreated);
    }


    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenuItem);

        String response = menuItemController.deleteMenuItem(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Menu item with id: 46e3e96a-7847-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        // given
        Optional<MenuItemDTO> expectedMenuItem = Optional.of(createChickenMenuItem().convertEntityToDTO());

        // when
        when(menuItemService.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenuItem);

        MenuItemResponse menuItemReturned = menuItemController.getMenuItemById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        MenuItemResponseAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() {
        // when
        Exception exception = assertThrows(MenuItemNotFoundException.class, () -> menuItemController.getMenuItemById(UUID.fromString("b41874c8-784d-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no menu item in restaurant with that id: b41874c8-784d-11ed-a1eb-0242ac120002", exception.getMessage());
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
        MenuItemResponseAssertions.checkAssertionsForMenuItemsInMenu(menuItemsReturned);
    }
}