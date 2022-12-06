package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuControllerTest {
    @Mock
    private MenuService menuService;

    private MenuController menuController;

    @BeforeEach
    void setup() {
        menuService = mock(MenuService.class);

        menuController = new MenuController(
          menuService
        );
    }

    @Test
    void shouldReturnAllMenu() {
        // given
        List<MenuDTO> expectedMenu = createMenuList()
                .stream()
                .map(Menu::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(menuService.findAll()).thenReturn(expectedMenu);

        List<MenuResponse> menuReturned = menuController.getMenus().getBody();

        // then
        assertEquals("Drinks", menuReturned.get(0).getName());
        assertEquals("Coke", menuReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menuReturned.get(1).getName());
        assertEquals("Chicken", menuReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menuReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menuReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menuReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = Optional.of(createMenu().convertEntityToDTO());

        // when
        when(menuService.findById(eq(1L))).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.getMenuById(1L).getBody();

        // then
        assertEquals("Drinks", menuReturned.getName());
        assertEquals("Coke", menuReturned.getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.getMenuItems().get(0).getPrice());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenu() {
        // when
        Exception exception = assertThrows(MenuNotFoundException.class, () -> menuController.getMenuById(3L));

        // then
        assertEquals("There are no menu in restaurant with that id: 3", exception.getMessage());
    }

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menu = createMenuRequest();
        MenuDTO expectedMenu = createMenu().convertEntityToDTO();

        // when
        when(menuService.save(menu)).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.saveMenu(menu).getBody();

        // then
        assertEquals("Drinks", menuReturned.getName());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = Optional.of(createMenu().convertEntityToDTO());

        // when
        when(menuService.findById(eq(1L))).thenReturn(expectedMenu);

        MenuResponse menuDeleted = menuController.deleteMenu(1L).getBody();

        // then
        assertEquals("Drinks", menuDeleted.getName());
        assertEquals("Coke", menuDeleted.getMenuItems().get(0).getName());
        assertEquals(1.99, menuDeleted.getMenuItems().get(0).getPrice());
    }
}
