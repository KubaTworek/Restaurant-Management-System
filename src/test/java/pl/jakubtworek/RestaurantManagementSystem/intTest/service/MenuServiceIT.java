package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenuRequest;

@SpringBootTest
@Transactional
class MenuServiceIT {
    @Autowired
    private MenuService menuService;

    @Test
    void shouldReturnCreatedMenu(){
        // given
        MenuRequest menu = createMenuRequest();

        // when
        MenuDTO menuCreated = menuService.save(menu);

        // then
        checkAssertionsForMenu(menuCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() throws MenuNotFoundException {
        // when
        menuService.deleteById(1L);
        List<MenuDTO> menuList = menuService.findAll();

        // then
        assertEquals(1, menuList.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllMenu() {
        // when
        List<MenuDTO> menuListReturned = menuService.findAll();

        // then
        checkAssertionsForMenus(menuListReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassId() {
        // when
        Optional<MenuDTO> menu = menuService.findById(1L);

        // then
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassName() {
        // when
        MenuDTO menuReturned = menuService.findByName("Drinks").orElse(null);

        // then
        checkAssertionsForMenu(menuReturned);
    }

    private void checkAssertionsForMenu(MenuDTO menu){
        assertEquals("Drinks", menu.getName());
        assertEquals("Coke", menu.getMenuItems().get(0).getName());
        assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
    }

    private void checkAssertionsForMenus(List<MenuDTO> menus){
        assertEquals("Drinks", menus.get(0).getName());
        assertEquals("Coke", menus.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menus.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menus.get(1).getName());
        assertEquals("Chicken", menus.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menus.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menus.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menus.get(1).getMenuItems().get(1).getPrice());
    }
}
