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
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menu = createMenuRequest();
        MenuDTO expectedMenu = createMenu().convertEntityToDTO();

        // when
        when(menuService.save(menu)).thenReturn(expectedMenu);

        MenuResponse menuCreated = menuController.saveMenu(menu).getBody();

        // then
        checkAssertionsForMenu(menuCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = Optional.of(createMenu().convertEntityToDTO());

        // when
        when(menuService.findById(1L)).thenReturn(expectedMenu);

        String response = menuController.deleteMenu(1L).getBody();

        // then
        assertEquals("Menu with id: 1 was deleted", response);
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
        checkAssertionsForMenus(menuReturned);
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        // given
        Optional<MenuDTO> expectedMenu = Optional.of(createMenu().convertEntityToDTO());

        // when
        when(menuService.findById(1L)).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.getMenuById(1L).getBody();

        // then
        checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldThrowException_whenMenuNotExist() {
        // when
        Exception exception = assertThrows(MenuNotFoundException.class, () -> menuController.getMenuById(3L));

        // then
        assertEquals("There are no menu in restaurant with that id: 3", exception.getMessage());
    }

    private void checkAssertionsForMenu(MenuResponse menu){
        assertEquals("Drinks", menu.getName());
        assertEquals("Coke", menu.getMenuItems().get(0).getName());
        assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
    }

    private void checkAssertionsForMenus(List<MenuResponse> menus){
        assertEquals("Drinks", menus.get(0).getName());
        assertEquals("Coke", menus.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menus.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menus.get(1).getName());
        assertEquals("Chicken", menus.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menus.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menus.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menus.get(1).getMenuItems().get(1).getPrice());
    }
}
