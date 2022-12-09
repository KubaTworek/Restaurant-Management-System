package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuItemNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuItemServiceImpl;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuItemServiceTest {
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuItemFactory menuItemFactory;

    private MenuItemService menuItemService;

    @BeforeEach
    void setup(){
        menuItemRepository = mock(MenuItemRepository.class);
        menuRepository = mock(MenuRepository.class);
        menuItemFactory= mock(MenuItemFactory.class);

        menuItemService = new MenuItemServiceImpl(
                menuItemRepository,
                menuRepository,
                menuItemFactory
        );
    }

    @Test
    void shouldReturnAllMenuItems() {
        // given
        List<MenuItem> menuItems = createMenuItemListForFood();

        // when
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        List<MenuItemDTO> menuItemsReturned = menuItemService.findAll();

        // then
        assertEquals(2,menuItemsReturned.size());
    }

    @Test
    void shouldReturnOneMenuItem() {
        // given
        Optional<MenuItem> menuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        Optional<MenuItemDTO> menuItemReturned = menuItemService.findById(1L);

        // then
        assertNotNull(menuItemReturned);
    }

/*    @Test
    void shouldReturnCreatedMenuItem(){
        // given
        Optional<Menu> expectedMenu = Optional.of(new Menu(2L, "Food", List.of()));
        Optional<MenuDTO> expectedMenuDTO = Optional.of(new MenuDTO(2L, "Food", List.of()));
        MenuItemRequest menuItem = new MenuItemRequest("Pizza", 12.99, "Food");
        MenuItem expectedMenuItem = new MenuItem(0L, "Pizza", 12.99, expectedMenu.get(), List.of());
        MenuItemDTO expectedMenuItemDTO = new MenuItemDTO(0L, "Pizza", 12.99, expectedMenuDTO.get(), List.of());

        when(menuRepository.findByName(eq("Food"))).thenReturn(expectedMenu);
        when(menuItemFactory.createMenuItem(menuItem, expectedMenuDTO.get())).thenReturn(expectedMenuItemDTO);
        when(menuItemRepository.save(expectedMenuItem)).thenReturn(expectedMenuItem);

        // when
        MenuItemDTO menuItemReturned = menuItemService.save(menuItem, expectedMenuDTO.get());

        // then
        assertEquals("Pizza", menuItemReturned.getName());
        assertEquals(12.99, menuItemReturned.getPrice());
        assertEquals("Food", menuItemReturned.getMenu().getName());
    }*/


    @Test
    void verifyIsMenuItemIsDeleted() throws MenuItemNotFoundException {
        // given
        Optional<MenuItem> menuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        menuItemService.deleteById(1L);

        // then
        verify(menuItemRepository).deleteById(1L);
    }

    @Test
    void shouldReturnOneMenuItem_whenMenuNameIsPass() {
        // given
        List<MenuItem> menuItems = createMenuItemListForFood();
        Optional<Menu> menu = Optional.of(createMenu());
        when(menuRepository.findByName("Menu")).thenReturn(menu);
        when(menuItemRepository.findByMenu(menu.get())).thenReturn(menuItems);

        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu(menu.get());

        // then
        assertEquals(2, menuItemsReturned.size());
    }
}
