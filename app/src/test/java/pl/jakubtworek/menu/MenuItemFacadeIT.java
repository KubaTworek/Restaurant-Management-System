package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuItemFacadeIT {
    @Mock
    private MenuItemRepository menuItemRepository;
    @Mock
    private MenuItemQueryRepository menuItemQueryRepository;
    @Mock
    private MenuQueryRepository menuQueryRepository;

    private MenuItemFacade menuItemFacade;

    private final MenuItem burger = createMenuItem();
    private final Menu food = createMenu();
    private final MenuItemDto burgerDto = MenuItemDto.create(1L, "Burger", BigDecimal.valueOf(10.00), Status.ACTIVE);
    private final MenuDto foodDto = MenuDto.create(1L, "Food", List.of(burgerDto));
    private final Set<MenuDto> menus = new HashSet<>();
    private final List<MenuItemDto> menuItems = createMenuItemDtos();

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItemFacade = new MenuItemFacade(
                new MenuItemFactory(menuItemRepository),
                menuItemRepository,
                menuItemQueryRepository,
                menuQueryRepository
        );
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(burger);
        when(menuItemRepository.save(any(Menu.class))).thenReturn(food);
        when(menuItemQueryRepository.findDtoById(1L)).thenReturn(Optional.of(burgerDto));
        when(menuItemQueryRepository.findDtoByName("Burger")).thenReturn(Optional.of(burgerDto));
        when(menuItemQueryRepository.findDtoByName("Pasta")).thenReturn(Optional.empty());
        when(menuItemQueryRepository.findByMenuName("Food")).thenReturn(menuItems);
        when(menuQueryRepository.findDtoByName("Food")).thenReturn(Optional.of(foodDto));
        when(menuQueryRepository.findDtoByName("Drinks")).thenReturn(Optional.empty());
        when(menuQueryRepository.findDtoByMenuItems_Status(Status.ACTIVE)).thenReturn(new HashSet<>());
    }

    @Test
    void shouldReturnMenuItemByName() {
        // when
        final var result = menuItemFacade.getByName("Burger");

        // then
        assertEquals(burgerDto, result);
    }

    @Test
    void shouldSaveMenuItemWithExistingMenu() {
        // given
        final var request = new MenuItemRequest("Pasta", BigDecimal.valueOf(10.00), "Food");

        // when
        final var result = menuItemFacade.save(request);

        // then
        assertEquals(burger.getSnapshot(1).getId(), result.getId());
        assertEquals(burger.getSnapshot(1).getName(), result.getName());
        assertEquals(burger.getSnapshot(1).getPrice(), result.getPrice());
        assertEquals(burger.getSnapshot(1).getStatus(), result.getStatus());
    }

    @Test
    void shouldSaveMenuItemWithNotExistingMenu() {
        // given
        final var request = new MenuItemRequest("Cola", BigDecimal.valueOf(10.00), "Drinks");

        // when
        final var result = menuItemFacade.save(request);

        // then
        assertEquals(burger.getSnapshot(1).getId(), result.getId());
        assertEquals(burger.getSnapshot(1).getName(), result.getName());
        assertEquals(burger.getSnapshot(1).getPrice(), result.getPrice());
        assertEquals(burger.getSnapshot(1).getStatus(), result.getStatus());
    }

    @Test
    void shouldDeleteMenuItem() {
        // when
        menuItemFacade.deleteById(1L);

        // then
        verify(menuItemRepository).deactivateMenuItem(1L);
    }

    @Test
    void shouldFindAllMenu() {
        // when
        final var result = new HashSet<>(menuItemFacade.findAll());

        // then
        assertEquals(menus, result);
    }

    @Test
    void shouldFindMenuItemById() {
        // when
        final var result = menuItemFacade.findById(1L);

        // then
        assertEquals(Optional.of(burgerDto), result);
    }

    @Test
    void shouldFindMenuByName() {
        // when
        final var result = menuItemFacade.findByMenu("Food");

        // then
        assertEquals(menuItems, result);
    }

    private MenuItem createMenuItem() {
        return MenuItem.restore(new MenuItemSnapshot(
                1L, "Burger", BigDecimal.valueOf(10.00), new MenuSnapshot(1L, "Food", new HashSet<>()), Status.INACTIVE
        ), 1);
    }

    private Menu createMenu() {
        return Menu.restore(new MenuSnapshot(
                1L, "Food", new HashSet<>()
        ), 1);
    }

    private List<MenuItemDto> createMenuItemDtos() {
        final List<MenuItemDto> menuItems = new ArrayList<>();
        menuItems.add(MenuItemDto.create(1L, "Spaghetti", new BigDecimal("10.00"), Status.ACTIVE));
        menuItems.add(MenuItemDto.create(2L, "Pizza", new BigDecimal("12.00"), Status.ACTIVE));
        menuItems.add(MenuItemDto.create(3L, "Salad", new BigDecimal("5.00"), Status.ACTIVE));
        return menuItems;
    }
}
