package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.MenuItemRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuItemFacadeTest {
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
        menuItemFacade = new MenuItemFacade(menuItemRepository, menuItemQueryRepository, menuQueryRepository);
    }

    @Test
    void shouldReturnMenuItemByName() {
        // given
        final var itemName = "Spaghetti";
        final var expectedMenuItem = new SimpleMenuItem(1L, itemName, 100);

        when(menuItemQueryRepository.findSimpleByName(itemName)).thenReturn(Optional.of(expectedMenuItem));

        // when
        final SimpleMenuItem result = menuItemFacade.getByName(itemName);

        // then
        assertEquals(expectedMenuItem, result);
    }

    @Test
    void shouldSaveMenuItem() {
        // given
        final var request = new MenuItemRequest("Lasagna", 140, "Dinner Menu");
        final var expectedMenu = MenuDto.create(1L, "Dinner Menu", null);
        final var expectedMenuItem = createMenuItem(1L, "Lasagna", 140, expectedMenu);

        when(menuQueryRepository.findDtoByName(request.getMenu())).thenReturn(Optional.of(expectedMenu));
        when(menuItemRepository.save(any())).thenReturn(expectedMenuItem);

        // when
        final MenuItemDto result = menuItemFacade.save(request);

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
    void shouldFindMenuItemById() {
        // given
        final var itemId = 1L;
        final var expectedMenuItem = MenuItemDto.create(itemId, "Pizza", 120);

        when(menuItemQueryRepository.findDtoById(itemId)).thenReturn(Optional.of(expectedMenuItem));

        // when
        final Optional<MenuItemDto> result = menuItemFacade.findById(itemId);

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
        final List<MenuItemDto> result = menuItemFacade.findByMenu(menuName);

        // then
        assertEquals(menuItems, result);
    }

    private MenuItem createMenuItem(Long id, String name, Integer price, MenuDto menuDto) {
        final var menuItem = new MenuItem();
        menuItem.setId(id);
        menuItem.setName(name);
        menuItem.setPrice(price);
        menuItem.setMenu(createMenu(menuDto));
        return menuItem;
    }

    private List<MenuItemDto> createMenuItemDtos() {
        final List<MenuItemDto> menuItems = new ArrayList<>();
        menuItems.add(MenuItemDto.create(1L, "Spaghetti", 100));
        menuItems.add(MenuItemDto.create(2L, "Pizza", 120));
        menuItems.add(MenuItemDto.create(3L, "Salad", 50));
        return menuItems;
    }

    private Menu createMenu(MenuDto menuDto) {
        final var menu = new Menu();
        menu.setId(menuDto.getId());
        menu.setName(menuDto.getName());
        return menu;
    }

    private void assertMenuItemEquals(final MenuItem expected, final MenuItemDto actual) {
        assertEquals(expected.getId(), actual.getId());
        assertEquals(expected.getName(), actual.getName());
        assertEquals(expected.getPrice(), actual.getPrice());
    }
}