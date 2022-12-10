package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenu;

@SpringBootTest
@Transactional
class MenuRepositoryIT {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    void shouldReturnCreatedMenu(){
        // given
        Menu menu = createMenu();

        // when
        Menu menuCreated = menuRepository.save(menu);

        // then
        checkAssertionsForMenu(menuCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuRepository.deleteById(1L);
        List<Menu> menuList = menuRepository.findAll();

        // then
        assertEquals(1, menuList.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllMenu() {
        // when
        List<Menu> menuListReturned = menuRepository.findAll();

        // then
        checkAssertionsForMenus(menuListReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassId() {
        // when
        Menu menuReturned = menuRepository.findById(1L).orElse(null);

        // then
        checkAssertionsForMenu(menuReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassName() {
        // when
        Menu menuReturned = menuRepository.findByName("Drinks").orElse(null);

        // then
        checkAssertionsForMenu(menuReturned);
    }

    private void checkAssertionsForMenu(Menu menu){
        assertEquals("Drinks", menu.getName());
        assertEquals("Coke", menu.getMenuItems().get(0).getName());
        assertEquals(1.99, menu.getMenuItems().get(0).getPrice());
    }

    private void checkAssertionsForMenus(List<Menu> menus){
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
