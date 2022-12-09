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

@SpringBootTest
@Transactional
class MenuServiceIT {
    @Autowired
    private MenuService menuService;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllMenu() {
        // when
        List<MenuDTO> menuList = menuService.findAll();

        // then
        assertEquals(2, menuList.size());

        assertEquals("Drinks", menuList.get(0).getName());
        assertEquals(1, menuList.get(0).getMenuItems().size());
        assertEquals("Coke", menuList.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menuList.get(0).getMenuItems().get(0).getPrice());

        assertEquals("Food", menuList.get(1).getName());
        assertEquals(2, menuList.get(1).getMenuItems().size());
        assertEquals("Chicken", menuList.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menuList.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Tiramisu", menuList.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menuList.get(1).getMenuItems().get(1).getPrice());
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
    void shouldReturnCreatedMenu(){
        // given
        MenuRequest menu = new MenuRequest("Alcohol");

        // when
        MenuDTO menuReturned = menuService.save(menu);

        // then
        assertEquals("Alcohol", menuReturned.getName());
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
    void shouldReturnOneMenu_whenPassName() {
        // when
        Optional<MenuDTO> menu = menuService.findByName("Drinks");

        // then
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }
}
