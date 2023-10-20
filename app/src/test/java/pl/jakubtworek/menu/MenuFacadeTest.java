package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuRequest;
import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class MenuFacadeTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuQueryRepository menuQueryRepository;

    private MenuFacade menuFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        menuFacade = new MenuFacade(menuRepository, menuQueryRepository);
    }

    @Test
    void testSave() {
        // given
        final var menuRequest = new MenuRequest("Breakfast Menu");
        final var menu = createMenu(1L, menuRequest.getName());
        when(menuRepository.save(any())).thenReturn(menu);

        // when
        final MenuDto result = menuFacade.save(menuRequest);

        // then
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
    }

    @Test
    void testDeleteById() {
        // given
        final var menuId = 1L;

        // when
        menuFacade.deleteById(menuId);

        // then
        verify(menuRepository).deleteById(menuId);
    }

    @Test
    void testFindAll() {
        // given
        final Set<MenuDto> menuList = new HashSet<>();
        when(menuQueryRepository.findBy(MenuDto.class)).thenReturn(menuList);

        // when
        final Set<MenuDto> result = new HashSet<>(menuFacade.findAll());

        // then
        assertEquals(menuList, result);
    }

    @Test
    void testFindById() {
        // given
        final var menuId = 1L;
        final var menuDto = MenuDto.create(menuId, "Lunch Menu", null);
        when(menuQueryRepository.findDtoById(menuId)).thenReturn(Optional.of(menuDto));

        // when
        final Optional<MenuDto> result = menuFacade.findById(menuId);

        // then
        assertEquals(Optional.of(menuDto), result);
    }

    @Test
    void testToDto() {
        // given
        final var menu = createMenu(1L, "Dinner menu");
        final List<SimpleMenuItem> menuItems = createSimpleMenuItems();
        menu.setMenuItems(menuItems);

        // when
        final MenuDto result = menuFacade.toDto(menu);

        // then
        assertEquals(menu.getId(), result.getId());
        assertEquals(menu.getName(), result.getName());
        assertEquals(menuItems.size(), result.getMenuItems().size());

        for (int i = 0; i < menuItems.size(); i++) {
            final SimpleMenuItem menuItem = menuItems.get(i);
            assertEquals(menuItem.getId(), result.getMenuItems().get(i).getId());
            assertEquals(menuItem.getName(), result.getMenuItems().get(i).getName());
            assertEquals(menuItem.getPrice(), result.getMenuItems().get(i).getPrice());
        }
    }

    private Menu createMenu(Long id, String name) {
        final var menu = new Menu();
        menu.setId(id);
        menu.setName(name);
        return menu;
    }

    private List<SimpleMenuItem> createSimpleMenuItems() {
        final List<SimpleMenuItem> menuItems = new ArrayList<>();
        menuItems.add(new SimpleMenuItem(1L, "Spaghetti", 100));
        menuItems.add(new SimpleMenuItem(2L, "Pizza", 120));
        menuItems.add(new SimpleMenuItem(3L, "Salad", 50));
        return menuItems;
    }
}