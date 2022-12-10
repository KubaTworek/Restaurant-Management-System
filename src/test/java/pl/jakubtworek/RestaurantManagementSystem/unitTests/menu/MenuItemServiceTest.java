package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuItemFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuItemServiceImpl;

import java.util.*;

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
    void shouldReturnCreatedMenuItem() throws MenuNotFoundException {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());
        MenuItemRequest menuItem = createChickenMenuItemRequest();
        MenuItem expectedMenuItem = createChickenMenuItem();
        MenuItemDTO expectedMenuItemDTO = createChickenMenuItem().convertEntityToDTO();

        // when
        when(menuRepository.findByName(any())).thenReturn(expectedMenu);
        when(menuItemFactory.createMenuItem(any(), any())).thenReturn(expectedMenuItemDTO);
        when(menuItemRepository.save(any())).thenReturn(expectedMenuItem);

        MenuItemDTO menuItemCreated = menuItemService.save(menuItem);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemCreated);
    }

    @Test
    void verifyIsMenuItemIsDeleted() throws MenuItemNotFoundException {
        // given
        Optional<MenuItem> menuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        menuItemService.deleteById(1L);

        // then
        verify(menuItemRepository).delete(any());
    }

    @Test
    void shouldReturnOneMenuItem() {
        // given
        Optional<MenuItem> menuItem = Optional.of(createChickenMenuItem());

        // when
        when(menuItemRepository.findById(1L)).thenReturn(menuItem);

        MenuItemDTO menuItemReturned = menuItemService.findById(1L).orElse(null);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnOneMenuItem_whenMenuNameIsPass() throws MenuNotFoundException {
        // given
        List<MenuItem> menuItems = createMenuItemListForFood();
        Optional<Menu> menu = Optional.of(createMenu());

        // when
        when(menuRepository.findByName("Food")).thenReturn(menu);
        when(menuItemRepository.findByMenu(menu.get())).thenReturn(menuItems);

        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu("Food");

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItems(menuItemsReturned);
    }
}
