package pl.jakubtworek.RestaurantManagementSystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class MenuServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MenuService menuService;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (1,'Drinks')");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (2,'Food')");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void findAllMenusTest(){
        Iterable<Menu> iterableMenus = menuService.findAll();

        List<Menu> menus = new ArrayList<>();

        for(Menu tempMenu : iterableMenus) {
            menus.add(tempMenu);
        }

        assertEquals(2,menus.size());
    }

    @Test
    public void findByIdMenuTest(){
        Menu menu = menuService.findById(2);

        assertEquals("Food",menu.getName());
    }

    @Test
    public void saveMenuTest(){
        Menu menu = new Menu();
        menu.setName("Meats");
        menuService.save(menu);

        Iterable<Menu> iterableMenus = menuService.findAll();

        List<Menu> menus = new ArrayList<>();

        for(Menu tempMenu : iterableMenus) {
            menus.add(tempMenu);
        }

        assertEquals(3,menus.size());
    }

    @Test
    public void deleteByIdMenuTest(){
        menuService.deleteById(2);

        Iterable<Menu> iterableMenus = menuService.findAll();

        List<Menu> menus = new ArrayList<>();

        for(Menu tempMenu : iterableMenus) {
            menus.add(tempMenu);
        }

        assertEquals(1,menus.size());
    }

    @Test
    public void findByNameMenuTest(){
        Menu menu = menuService.findByName("Food");

        assertEquals("Food",menu.getName());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Menu");
    }
}
