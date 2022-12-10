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
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

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
        assertEquals("Food", menuCreated.getName());
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
        MenuDTOAssertions.checkAssertionsForMenus(menuListReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassId() {
        // when
        MenuDTO menuReturned = menuService.findById(2L).orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenu_whenPassName() {
        // when
        MenuDTO menuReturned = menuService.findByName("Food").orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }
}
