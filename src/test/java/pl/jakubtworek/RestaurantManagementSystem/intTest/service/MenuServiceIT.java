package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuServiceIT {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuRepository menuRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllMenu() {
        // when
        List<Menu> menuList = menuService.findAll();

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
    public void shouldReturnOneMenu_whenPassId() {
        // when
        Optional<Menu> menu = menuService.findById(1L);

        // then
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }

    @Test
    public void shouldReturnCreatedMenu(){
        // given
        MenuRequest menu = new MenuRequest(0L, "Alcohol");

        // when
        Menu menuReturned = menuService.save(menu);

        // then
        assertEquals("Alcohol", menuReturned.getName());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuService.deleteById(1L);
        List<Menu> menuList = menuService.findAll();

        // then
        assertEquals(1, menuList.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneMenu_whenPassName() {
        // when
        Optional<Menu> menu = menuService.findByName("Drinks");

        // then
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }
}
