package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
class MenuControllerIT {
    @Autowired
    private MenuController menuController;

    @MockBean
    private MenuRepository menuRepository;

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menuRequest = createMenuRequest();
        Menu menuCreated = createMenu();

        // when
        when(menuRepository.save(any())).thenReturn(menuCreated);

        MenuResponse menuReturned = menuController.saveMenu(menuRequest).getBody();

        // then
        MenuResponseAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(1L)).thenReturn(expectedMenu);

        String response = menuController.deleteMenu(1L).getBody();

        // then
        assertEquals("Menu with id: 1 was deleted", response);
    }

    @Test
    void shouldReturnAllMenu() {
        // given
        List<Menu> expectedMenu = createMenuList();

        // when
        when(menuRepository.findAll()).thenReturn(expectedMenu);

        List<MenuResponse> menuReturned = menuController.getMenus().getBody();

        // then
        MenuResponseAssertions.checkAssertionsForMenus(menuReturned);
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(1L)).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.getMenuById(1L).getBody();

        // then
        MenuResponseAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldThrowException_whenMenuNotExist() {
        // when
        Exception exception = assertThrows(MenuNotFoundException.class, () -> menuController.getMenuById(3L));

        // then
        assertEquals("There are no menu in restaurant with that id: 3", exception.getMessage());
    }
}
