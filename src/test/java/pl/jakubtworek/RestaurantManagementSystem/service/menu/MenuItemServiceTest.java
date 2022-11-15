package pl.jakubtworek.RestaurantManagementSystem.service.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuItemServiceImp;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

public class MenuItemServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private MenuItemFactory menuItemFactory;
    private MenuItemService menuItemService;

    @BeforeEach
    public void setUp() {
        menuRepository = mock(MenuRepository.class);
        menuItemRepository = mock(MenuItemRepository.class);
        menuItemFactory = mock(MenuItemFactory.class);

        menuItemService = new MenuItemServiceImp(
                menuItemRepository,
                menuRepository,
                menuItemFactory);
    }

    @Test
    public void shouldReturnAllMenuItems() {
        // given
        List<MenuItem> menuItems = createMenuItems();
        when(menuItemRepository.findAll()).thenReturn(menuItems);

        // when
        List<MenuItem> menuItemsReturned = menuItemService.findAll();

        // then
        assertEquals(3,menuItemsReturned.size());
    }

    @Test
    public void shouldReturnOneMenuItem() {
        // given
        Optional<MenuItem> menuItem = Optional.of(new MenuItem());
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        // when
        Optional<MenuItem> menuItemReturned = menuItemService.findById(1L);

        // then
        assertNotNull(menuItemReturned);
    }

    @Test
    public void shouldReturnCreatedMenuItem(){
        // given
        Optional<Menu> expectedMenu = Optional.of(new Menu(2L, "Food", List.of()));
        MenuItemRequest menuItem = new MenuItemRequest(0L, "Pizza", 12.99, "Food");
        MenuItem expectedMenuItem = new MenuItem(0L, "Pizza", 12.99, expectedMenu.get(), List.of());

        when(menuRepository.findByName(eq("Food"))).thenReturn(expectedMenu);
        when(menuItemFactory.createMenuItem(menuItem, expectedMenu.get())).thenReturn(expectedMenuItem);
        when(menuItemRepository.save(expectedMenuItem)).thenReturn(expectedMenuItem);

        // when
        MenuItem menuItemReturned = menuItemService.save(menuItem);

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
        List<MenuItem> menuItemsReturned = menuItemService.findByMenu("Menu");

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
