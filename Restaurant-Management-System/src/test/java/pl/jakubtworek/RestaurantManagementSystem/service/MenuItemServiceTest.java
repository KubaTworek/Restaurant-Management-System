package pl.jakubtworek.RestaurantManagementSystem.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.TestPropertySource;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;


import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@TestPropertySource("/application-test.properties")
@SpringBootTest
public class MenuItemServiceTest {

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MenuItemService menuItemService;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (1,'Drinks')");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (2,'Food')");
        jdbc.execute("INSERT INTO Menu_Item(id, name, price, menu_id)" + "VALUES (1,'Coke', 1.29, 1)");
        jdbc.execute("INSERT INTO Menu_Item(id, name, price, menu_id)" + "VALUES (2,'Chicken', 8.99, 2)");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void findAllMenuItemsTest(){
        Iterable<MenuItem> iterableMenuItems = menuItemService.findAll();

        List<MenuItem> menuItems = new ArrayList<>();

        for(MenuItem tempMenuItem : iterableMenuItems) {
            menuItems.add(tempMenuItem);
        }

        assertEquals(2,menuItems.size());
    }

    @Test
    public void findByIdMenuItemTest(){
        MenuItem menuItem = menuItemService.findById(2);

        assertEquals("Chicken",menuItem.getName());
    }

    @Test
    public void saveMenuItemTest(){
        MenuItem menuItem = new MenuItem();
        menuItem.setName("Meats");
        menuItem.setPrice(2.5);
        menuItemService.save(menuItem);

        Iterable<MenuItem> iterableMenuItems = menuItemService.findAll();

        List<MenuItem> menuItems = new ArrayList<>();

        for(MenuItem tempMenuItem : iterableMenuItems) {
            menuItems.add(tempMenuItem);
        }

        assertEquals(3,menuItems.size());
    }

    @Test
    public void deleteByIdMenuItemTest(){
        menuItemService.deleteById(2);

        Iterable<MenuItem> iterableMenuItems = menuItemService.findAll();

        List<MenuItem> menuItems = new ArrayList<>();

        for(MenuItem tempMenuItem : iterableMenuItems) {
            menuItems.add(tempMenuItem);
        }

        assertEquals(1,menuItems.size());
    }

    @Test
    public void findByMenuMenuItemTest(){
        List<MenuItem> menuItems = menuItemService.findByMenu("Food");

        assertEquals(1,menuItems.size());
    }

    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Menu_Item");
        jdbc.execute("DELETE FROM Menu");
    }
}
