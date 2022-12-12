package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
        assertEquals("Food", menuReturned.getName());
    }

    @Test
    void shouldReturnUpdatedMenu() {
        // given
        MenuRequest menuRequest = createMenuRequest();
        Menu menuCreated = createMenu();

        // when
        when(menuRepository.save(any())).thenReturn(menuCreated);
        when(menuRepository.getReferenceById(any())).thenReturn(menuCreated);

        MenuResponse menuReturned = menuController.updateMenu(menuRequest, UUID.randomUUID()).getBody();

        // then
        assertEquals("Food", menuReturned.getName());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // given
        Optional<Menu> expectedMenu = Optional.of(createMenu());

        // when
        when(menuRepository.findById(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenu);

        String response = menuController.deleteMenu(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Menu with id: 31da2070-7847-11ed-a1eb-0242ac120002 was deleted", response);
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
        when(menuRepository.findById(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002"))).thenReturn(expectedMenu);

        MenuResponse menuReturned = menuController.getMenuById(UUID.fromString("31da2070-7847-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Food", menuReturned.getName());
    }

    @Test
    void shouldThrowException_whenMenuNotExist() {
        // when
        Exception exception = assertThrows(MenuNotFoundException.class, () -> menuController.getMenuById(UUID.fromString("b41874c8-784d-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no menu in restaurant with that id: b41874c8-784d-11ed-a1eb-0242ac120002", exception.getMessage());
    }
}
