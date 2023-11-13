package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;

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
        MockitoAnnotations.initMocks(this);
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
        final var expectedMenuItem = MenuItemDto.create(1L, itemName, 100);

        when(menuItemQueryRepository.findDtoByName(itemName)).thenReturn(Optional.of(expectedMenuItem));

        // when
        final var result = menuItemFacade.getByName(itemName);

        // then
        assertEquals(expectedMenuItem, result);
    }

    @Test
    void shouldSaveMenuItemWithExistingMenu() {
        // given
        final var request = new MenuItemRequest("Lasagna", 140, "Dinner Menu");
        final var expectedMenu = MenuDto.create(1L, "Dinner Menu", null);
        final var expectedMenuItem = createMenuItem(1L, "Lasagna", 140, expectedMenu);

        when(menuQueryRepository.findDtoByName(request.getMenu())).thenReturn(Optional.of(expectedMenu));
        when(menuItemRepository.save(any(MenuItem.class))).thenReturn(expectedMenuItem);

        // when
        final var result = menuItemFacade.save(request);

        // then
        assertMenuItemEquals(expectedMenuItem, result);
    }

    @Test
    void shouldSaveMenuItemWithNotExistingMenu() {
        // given
        final var request = new MenuItemRequest("Lasagna", 140, "Dinner Menu");
        final var expectedMenuDto = MenuDto.create(1L, "Dinner Menu", null);
        final var expectedMenuItem = createMenuItem(1L, "Lasagna", 140, expectedMenuDto);

        when(menuQueryRepository.findDtoByName(request.getMenu())).thenReturn(Optional.empty());
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
        verify(menuItemRepository).deleteById(itemId);
    }

    @Test
    void shouldFindAllMenu() {
        // given
        final Set<MenuDto> expectedMenuList = new HashSet<>();

        when(menuQueryRepository.findBy(MenuDto.class)).thenReturn(expectedMenuList);

        // when
        final var result = new HashSet<>(menuItemFacade.findAll());

        // then
        assertEquals(expectedMenuList, result);
    }

    @Test
    void shouldFindMenuItemById() {
        // given
        final var itemId = 1L;
        final var expectedMenuItem = MenuItemDto.create(itemId, "Pizza", 120);

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

    private MenuItem createMenuItem(Long id, String name, Integer price, MenuDto menuDto) {
        return MenuItem.restore(new MenuItemSnapshot(
                id, name, price, createMenu(menuDto).getSnapshot(), new HashSet<>()
        ));
    }

    private List<MenuItemDto> createMenuItemDtos() {
        final List<MenuItemDto> menuItems = new ArrayList<>();
        menuItems.add(MenuItemDto.create(1L, "Spaghetti", 100));
        menuItems.add(MenuItemDto.create(2L, "Pizza", 120));
        menuItems.add(MenuItemDto.create(3L, "Salad", 50));
        return menuItems;
    }

    private MenuItem.Menu createMenu(MenuDto menuDto) {
        return MenuItem.Menu.restore(new MenuSnapshot(
                menuDto.getId(), menuDto.getName(), new HashSet<>()
        ));
    }

    private void assertMenuItemEquals(final MenuItem expected, final MenuItemDto actual) {
        assertEquals(expected.getSnapshot().getId(), actual.getId());
        assertEquals(expected.getSnapshot().getName(), actual.getName());
        assertEquals(expected.getSnapshot().getPrice(), actual.getPrice());
    }
}