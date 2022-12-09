package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuFactory menuFactory;

    private MenuService menuService;

    @BeforeEach
    void setup(){
        menuRepository = mock(MenuRepository.class);
        menuFactory = mock(MenuFactory.class);

        menuService = new MenuServiceImpl(
                menuRepository,
                menuFactory
        );
    }

    @Test
    void shouldReturnCreatedMenu(){
        // given
        MenuRequest menu = createMenuRequest();
        Menu expectedMenu = createMenu();
        MenuDTO expectedMenuDTO = createMenu().convertEntityToDTO();

        // when
        when(menuFactory.createMenu(any())).thenReturn(expectedMenuDTO);
        when(menuRepository.save(any())).thenReturn(expectedMenu);

        MenuDTO menuCreated = menuService.save(menu);

        // then
        checkAssertionsForMenu(menuCreated);
    }


    @Test
    void verifyIsMenuIsDeleted() throws MenuNotFoundException {
        // given
        Menu menu = createMenu();

        // when
        when(menuRepository.findById(1L)).thenReturn(Optional.of(menu));

        menuService.deleteById(1L);

        // then
        verify(menuRepository).delete(menu);
    }

    @Test
    void shouldReturnAllMenus() {
        // given
        List<Menu> menus = createMenuList();

        // when
        when(menuRepository.findAll()).thenReturn(menus);

        List<MenuDTO> menuReturned = menuService.findAll();

        // then
        checkAssertionsForMenus(menuReturned);
    }

    @Test
    void shouldReturnMenuById() {
        // given
        Optional<Menu> menu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(1L)).thenReturn(menu);

        MenuDTO menuReturned = menuService.findById(1L).orElse(null);

        // then
        checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldReturnMenuByName() {
        // given
        Optional<Menu> menu = Optional.of(createMenu());

        // when
        when(menuRepository.findByName("Menu")).thenReturn(menu);

        MenuDTO menuReturned = menuService.findByName("Menu").orElse(null);

        // then
        checkAssertionsForMenu(menuReturned);
    }

    private void checkAssertionsForMenu(MenuDTO menu){
        assertEquals("Drinks", menu.getName());
        assertEquals("Coke", menu.getMenuItems().get(0).getName());
        assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
    }

    private void checkAssertionsForMenus(List<MenuDTO> menus){
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
