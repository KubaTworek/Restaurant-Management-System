package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
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
    void shouldReturnAllMenu() {
        // given
        List<Menu> expectedMenu = createMenuList();

        // when
        when(menuRepository.findAll()).thenReturn(expectedMenu);

        List<MenuResponse> menuReturned = menuController.getMenus().getBody();

        // then
        assertEquals("Drinks", menuReturned.get(0).getName());
        assertEquals("Coke", menuReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menuReturned.get(1).getName());
        assertEquals("Chicken", menuReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menuReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menuReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menuReturned.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(1L)).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.getMenuById(1L).getBody();

        // then
        assertEquals("Drinks", menuReturned.getName());
        assertEquals("Coke", menuReturned.getMenuItems().get(0).getName());
        assertEquals(1.99, menuReturned.getMenuItems().get(0).getPrice());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenu() {
        // when
        Exception exception = assertThrows(MenuNotFoundException.class, () -> menuController.getMenuById(3L));

        // then
        assertEquals("There are no menu in restaurant with that id: 3", exception.getMessage());
    }

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menuRequest = createMenuRequest();
        Menu menuCreated = createMenu();

        // when
        when(menuRepository.save(any())).thenReturn(menuCreated);

        MenuResponse menuReturned = menuController.saveMenu(menuRequest).getBody();

        // then
        assertEquals("Drinks", menuReturned.getName());
    }


    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(1L)).thenReturn(expectedMenu);

        MenuResponse menuDeleted = menuController.deleteMenu(1L).getBody();

        // then
        assertEquals("Drinks", menuDeleted.getName());
        assertEquals("Coke", menuDeleted.getMenuItems().get(0).getName());
        assertEquals(1.99, menuDeleted.getMenuItems().get(0).getPrice());
    }
}
