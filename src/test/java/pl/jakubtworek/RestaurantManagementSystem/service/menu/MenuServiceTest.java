package pl.jakubtworek.RestaurantManagementSystem.service.menu;

import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class MenuServiceTest {

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private MenuFactory menuFactory;

    @Autowired
    private MenuService menuService;


    @Test
    public void shouldReturnAllMenus() {
        // given
        List<Menu> menus = createMenus();
        when(menuRepository.findAll()).thenReturn(menus);

        // when
        List<MenuDTO> menusReturned = menuService.findAll();

        // then
        assertEquals(3,menusReturned.size());
    }

    @Test
    public void shouldReturnOneMenu() {
        // given
        Optional<Menu> menu = Optional.of(new Menu());
        when(menuRepository.findById(1L)).thenReturn(menu);

        // when
        Optional<MenuDTO> menuReturned = menuService.findById(1L);

        // then
        assertNotNull(menuReturned);
    }

    @Test
    public void shouldReturnCreatedMenu(){
        // given
        MenuRequest menu = new MenuRequest("Alcohol");
        Menu expectedMenu = new Menu(0L, "Alcohol", List.of());
        MenuDTO expectedMenuDTO = new MenuDTO(0L, "Alcohol", List.of());

        when(menuFactory.createMenu(menu)).thenReturn(expectedMenuDTO);
        when(menuRepository.save(expectedMenu)).thenReturn(expectedMenu);

        // when
        MenuDTO menuReturned = menuService.save(menu);

        // then
        assertEquals("Alcohol", menuReturned.getName());
    }


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
        Optional<MenuDTO> menuReturned = menuService.findByName("Menu");

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
