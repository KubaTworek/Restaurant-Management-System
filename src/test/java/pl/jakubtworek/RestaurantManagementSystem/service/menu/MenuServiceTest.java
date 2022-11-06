package pl.jakubtworek.RestaurantManagementSystem.service.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.MenuServiceImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    private MenuService menuService;

    @BeforeEach
    public void setUp() {
        menuRepository = mock(MenuRepository.class);

        menuService = new MenuServiceImpl(
                menuRepository,
                menuFactory);
    }

    @Test
    public void shouldReturnAllMenus() {
        // given
        List<Menu> menus = createMenus();
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<Menu> menusReturned = menuService.findAll();

        // then
        assertEquals(3,menusReturned.size());
    }

    @Test
    public void shouldReturnOneMenu() {
        // given
        Optional<Menu> menu = Optional.of(new Menu());
        when(menuRepository.findById(1L)).thenReturn(menu);

        // when
        Optional<Menu> menuReturned = menuService.findById(1L);

        // then
        assertNotNull(menuReturned);
    }

/*    @Test
    public void shouldReturnCreatedMenu(){
        // given
        Menu menu = spy(new Menu());
        MenuDTO menuDTO = spy(new MenuDTO());
        when(menuRepository.save(menu)).thenReturn(menu);

        // when
        Menu menuReturned = menuService.save(menuDTO);

        // then
        assertNotNull(menuReturned);
    }*/


    @Test
    public void verifyIsMenuIsDeleted(){
        // when
        menuService.deleteById(1L);

        // then
        verify(menuRepository).deleteById(1L);
    }

    @Test
    public void shouldReturnOneMenu_whenNameIsPass() {
        // given
        Optional<Menu> menu = Optional.of(new Menu());
        when(menuRepository.findByName("Menu")).thenReturn(menu);

        // when
        Optional<Menu> menuReturned = menuService.findByName("Menu");

        // then
        assertNotNull(menuReturned);
    }

    private List<Menu> createMenus() {
        List<Menu> menus = new ArrayList<>();
        Menu menu1 = new Menu();
        Menu menu2 = new Menu();
        Menu menu3 = new Menu();
        menus.add(menu1);
        menus.add(menu2);
        menus.add(menu3);
        return menus;
    }
}
