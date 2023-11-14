package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.order.dto.Status;

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

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        menuItemFacade = new MenuItemFacade(
                new MenuItemFactory(menuItemRepository),
                menuItemRepository,
                menuItemQueryRepository,
                menuQueryRepository
        );
    }

    @Test
    void shouldReturnMenuItemByName() {
        // given
        final var itemName = "Spaghetti";
        final var expectedMenuItem = MenuItemDto.create(1L, itemName, new BigDecimal("10.00"), Status.ACTIVE);

        when(menuItemQueryRepository.findDtoByName(itemName)).thenReturn(Optional.of(expectedMenuItem));

        // when
        final var result = menuItemFacade.getByName(itemName);

        // then
        assertEquals(expectedMenuItem, result);
    }

    @Test
    void shouldSaveMenuItemWithExistingMenu() {
        // given
        final var request = new MenuItemRequest("Lasagna", new BigDecimal("14.00"), "Dinner Menu");
        final var expectedMenu = MenuDto.create(1L, "Dinner Menu", null);
        final var expectedMenuItem = createMenuItem(1L, "Lasagna", new BigDecimal("14.00"), expectedMenu);

        when(menuQueryRepository.findDtoByName(request.menu())).thenReturn(Optional.of(expectedMenu));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(expectedMenuItem);

        // when
        final var result = menuItemFacade.save(request);

        // then
        assertMenuItemEquals(expectedMenuItem, result);
    }

    @Test
    void shouldSaveMenuItemWithNotExistingMenu() {
        // given
        final var request = new MenuItemRequest("Lasagna", new BigDecimal("14.00"), "Dinner Menu");
        final var expectedMenuDto = MenuDto.create(1L, "Dinner Menu", null);
        final var expectedMenuItem = createMenuItem(1L, "Lasagna", new BigDecimal("14.00"), expectedMenuDto);

        when(menuQueryRepository.findDtoByName(request.menu())).thenReturn(Optional.empty());
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(expectedMenuItem);

        // when
        final var result = menuItemFacade.save(request);

        // then
        assertMenuItemEquals(expectedMenuItem, result);
    }

    @Test
    void shouldDeleteMenuItem() {
        // given
        final var itemId = 1L;

        // when
        menuItemFacade.deleteById(itemId);

        // then
        verify(menuItemRepository).deactivateMenuItem(itemId);
    }

    @Test
    void shouldFindAllMenu() {
        // given
        final Set<MenuDto> expectedMenuList = new HashSet<>();

        when(menuQueryRepository.findDtoByMenuItems_Status(Status.ACTIVE)).thenReturn(expectedMenuList);

        // when
        final var result = new HashSet<>(menuItemFacade.findAll());

        // then
        assertEquals(expectedMenuList, result);
    }

    @Test
    void shouldFindMenuItemById() {
        // given
        final var itemId = 1L;
        final var expectedMenuItem = MenuItemDto.create(itemId, "Pizza", new BigDecimal("12.00"), Status.ACTIVE);

        when(menuItemQueryRepository.findDtoById(itemId)).thenReturn(Optional.of(expectedMenuItem));

        // when
        final var result = menuItemFacade.findById(itemId);

        // then
        assertEquals(Optional.of(expectedMenuItem), result);
    }

    @Test
    void shouldFindMenuByName() {
        // given
        final var menuName = "Lunch Menu";
        final List<MenuItemDto> menuItems = createMenuItemDtos();

        when(menuItemQueryRepository.findByMenuName(menuName)).thenReturn(menuItems);

        // when
        final var result = menuItemFacade.findByMenu(menuName);

        // then
        assertEquals(menuItems, result);
    }

    private MenuItem createMenuItem(Long id, String name, BigDecimal price, MenuDto menuDto) {
        return MenuItem.restore(new MenuItemSnapshot(
                id, name, price, createMenu(menuDto).getSnapshot(1), Status.ACTIVE, new HashSet<>()
        ), 1);
    }

    private List<MenuItemDto> createMenuItemDtos() {
        final List<MenuItemDto> menuItems = new ArrayList<>();
        menuItems.add(MenuItemDto.create(1L, "Spaghetti", new BigDecimal("10.00"), Status.ACTIVE));
        menuItems.add(MenuItemDto.create(2L, "Pizza", new BigDecimal("12.00"), Status.ACTIVE));
        menuItems.add(MenuItemDto.create(3L, "Salad", new BigDecimal("5.00"), Status.ACTIVE));
        return menuItems;
    }

    private MenuItem.Menu createMenu(MenuDto menuDto) {
        return MenuItem.Menu.restore(new MenuSnapshot(
                menuDto.getId(), menuDto.getName(), new HashSet<>()
        ), 1);
    }

    private void assertMenuItemEquals(final MenuItem expected, final MenuItemDto actual) {
        assertEquals(expected.getSnapshot(1).getId(), actual.getId());
        assertEquals(expected.getSnapshot(1).getName(), actual.getName());
        assertEquals(expected.getSnapshot(1).getPrice(), actual.getPrice());
    }
}
