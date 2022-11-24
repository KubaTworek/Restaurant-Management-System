package pl.jakubtworek.RestaurantManagementSystem.service.menu;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuItemServiceImp;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MenuItemServiceTest {
    private MenuRepository menuRepository;
    private MenuItemRepository menuItemRepository;
    private MenuItemFactory menuItemFactory;

    private MenuItemService menuItemService;

    @BeforeEach
    public void setup(){
        menuRepository = mock(MenuRepository.class);

        menuItemRepository = mock(MenuItemRepository.class);
        menuItemFactory= mock(MenuItemFactory.class);

        menuItemService = new MenuItemServiceImp(
                menuItemRepository,
                menuItemFactory
        );
    }

    @Test
    public void shouldReturnAllMenuItems() {
        // given
        List<MenuItem> menuItems = createMenuItems();
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findAll();

        // then
        assertEquals(3,menuItemsReturned.size());
    }

    @Test
    public void shouldReturnOneMenuItem() {
        // given
        Optional<MenuItem> menuItem = Optional.of(new MenuItem());
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        // when
        Optional<MenuItemDTO> menuItemReturned = menuItemService.findById(1L);

        // then
        assertNotNull(menuItemReturned);
    }

    @Test
    public void shouldReturnCreatedMenuItem(){
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
    }


    @Test
    public void verifyIsMenuItemIsDeleted(){
        // when
        menuItemService.deleteById(1L);

        // then
        verify(menuItemRepository).deleteById(1L);
    }

    @Test
    public void shouldReturnOneMenuItem_whenMenuNameIsPass() {
        // given
        List<MenuItem> menuItems = createMenuItems();
        Optional<Menu> menu = Optional.of(new Menu());
        when(menuRepository.findByName("Menu")).thenReturn(menu);
        when(menuItemRepository.findByMenu(menu.get())).thenReturn(menuItems);

        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu(menu.get());

        // then
        assertEquals(3, menuItemsReturned.size());
    }

    private List<MenuItem> createMenuItems() {
        List<MenuItem> menuItems = new ArrayList<>();
        MenuItem menuItem1 = new MenuItem();
        MenuItem menuItem2 = new MenuItem();
        MenuItem menuItem3 = new MenuItem();
        menuItems.add(menuItem1);
        menuItems.add(menuItem2);
        menuItems.add(menuItem3);
        return menuItems;
    }
}
