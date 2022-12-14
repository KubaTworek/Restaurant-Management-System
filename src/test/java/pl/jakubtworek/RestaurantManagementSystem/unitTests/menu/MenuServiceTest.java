package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuFactory menuFactory;

    private MenuService menuService;

    @BeforeEach
    void setup() {
        menuRepository = mock(MenuRepository.class);
        menuFactory = mock(MenuFactory.class);

        menuService = new MenuServiceImpl(
                menuRepository,
                menuFactory
        );
    }

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menu = createMenuRequest();
        Menu expectedMenu = createMenu();
        MenuDTO expectedMenuDTO = createMenu().convertEntityToDTO();

        // when
        when(menuFactory.createMenu(any())).thenReturn(expectedMenuDTO);
        when(menuRepository.save(any())).thenReturn(expectedMenu);

        MenuDTO menuCreated = menuService.save(menu);

        // then
        assertEquals("Food", menuCreated.getName());
    }

    @Test
    void shouldReturnUpdatedMenu() {
        // given
        MenuRequest menu = createMenuRequest();
        Menu expectedMenu = createMenu();
        MenuDTO expectedMenuDTO = createMenu().convertEntityToDTO();

        // when
        when(menuFactory.updateMenu(any(), any())).thenReturn(expectedMenuDTO);
        when(menuRepository.save(any())).thenReturn(expectedMenu);

        MenuDTO menuCreated = menuService.update(menu, UUID.randomUUID());

        // then
        assertEquals("Food", menuCreated.getName());
    }


    @Test
    void verifyIsMenuIsDeleted() throws MenuNotFoundException {
        // given
        Menu menu = createMenu();

        // when
        when(menuRepository.findById(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002"))).thenReturn(Optional.of(menu));

        menuService.deleteById(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002"));

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
        MenuDTOAssertions.checkAssertionsForMenus(menuReturned);
    }

    @Test
    void shouldReturnMenuById() {
        // given
        Optional<Menu> menu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002"))).thenReturn(menu);

        MenuDTO menuReturned = menuService.findById(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldReturnMenuByName() {
        // given
        Optional<Menu> menu = Optional.of(createMenu());

        // when
        when(menuRepository.findByName("Food")).thenReturn(menu);

        MenuDTO menuReturned = menuService.findByName("Food").orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }
}