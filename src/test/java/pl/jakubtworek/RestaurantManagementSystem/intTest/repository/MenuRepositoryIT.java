package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Sql(scripts = "/schema.sql")
public class MenuRepositoryIT {

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllMenu() {
        // when
        List<Menu> menuList = menuRepository.findAll();

        // then
        assertEquals(2, menuList.size());
        assertEquals(1, menuList.get(0).getId());
        assertEquals("Drinks", menuList.get(0).getName());
        assertEquals(1, menuList.get(0).getMenuItems().size());
        assertEquals(2, menuList.get(0).getMenuItems().get(0).getId());
        assertEquals("Coke", menuList.get(0).getMenuItems().get(0).getName());
        assertEquals(1.99, menuList.get(0).getMenuItems().get(0).getPrice());
        assertEquals(2, menuList.get(1).getId());
        assertEquals("Food", menuList.get(1).getName());
        assertEquals(2, menuList.get(1).getMenuItems().size());
        assertEquals(1, menuList.get(1).getMenuItems().get(0).getId());
        assertEquals("Chicken", menuList.get(1).getMenuItems().get(0).getName());
        assertEquals(10.99, menuList.get(1).getMenuItems().get(0).getPrice());
        assertEquals(3, menuList.get(1).getMenuItems().get(1).getId());
        assertEquals("Tiramisu", menuList.get(1).getMenuItems().get(1).getName());
        assertEquals(5.99, menuList.get(1).getMenuItems().get(1).getPrice());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneMenu_whenPassId() {
        // when
        Optional<Menu> menu = menuRepository.findById(1L);

        // then
        assertNotNull(menu.get());
        assertEquals(1, menu.get().getId());
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals(2, menu.get().getMenuItems().get(0).getId());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnHigherSizeOfList_whenCreateOne(){

    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuRepository.deleteById(1L);
        List<Menu> menuList = menuRepository.findAll();

        // then
        assertEquals(1, menuList.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneMenu_whenPassName() {
        // when
        Optional<Menu> menu = menuRepository.findByName("Drinks");

        // then
        assertNotNull(menu.get());
        assertEquals(1, menu.get().getId());
        assertEquals("Drinks", menu.get().getName());
        assertEquals(1, menu.get().getMenuItems().size());
        assertEquals(2, menu.get().getMenuItems().get(0).getId());
        assertEquals("Coke", menu.get().getMenuItems().get(0).getName());
        assertEquals(1.99, menu.get().getMenuItems().get(0).getPrice());
    }
}
