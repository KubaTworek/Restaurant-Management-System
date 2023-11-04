package pl.jakubtworek.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.menu.dto.MenuDto;
import pl.jakubtworek.menu.dto.MenuRequest;

import java.util.HashSet;
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
    void shouldSaveMenu() {
        // given
        final var request = new MenuRequest("Breakfast Menu");
        final var expectedMenu = createMenu(1L, request.getName());

        when(menuRepository.save(any())).thenReturn(expectedMenu);

        // when
        final MenuDto result = menuFacade.save(request);

        // then
        assertEquals(expectedMenu.getSnapshot().getId(), result.getId());
        assertEquals(expectedMenu.getSnapshot().getName(), result.getName());
    }

    @Test
    void shouldDeleteMenu() {
        // given
        final var menuId = 1L;

        // when
        menuFacade.deleteById(menuId);

        // then
        verify(menuRepository).deleteById(menuId);
    }

    @Test
    void shouldFindAllMenu() {
        // given
        final Set<MenuDto> expectedMenuList = new HashSet<>();

        when(menuQueryRepository.findBy(MenuDto.class)).thenReturn(expectedMenuList);

        // when
        final Set<MenuDto> result = new HashSet<>(menuFacade.findAll());

        // then
        assertEquals(expectedMenuList, result);
    }

    @Test
    void shouldFindMenuById() {
        // given
        final var menuId = 1L;
        final var expectedMenu = MenuDto.create(menuId, "Lunch Menu", null);

        when(menuQueryRepository.findDtoById(menuId)).thenReturn(Optional.of(expectedMenu));

        // when
        final Optional<MenuDto> result = menuFacade.findById(menuId);

        // then
        assertEquals(Optional.of(expectedMenu), result);
    }

    private Menu createMenu(Long id, String name) {
        final var menu = new Menu();
        menu.updateInfo(id, name);
        return menu;
    }
}