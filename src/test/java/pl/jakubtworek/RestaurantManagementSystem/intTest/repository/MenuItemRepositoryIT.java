package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.spy;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuItemRepositoryIT {

    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllMenuItems() {
        // when
        List<MenuItem> menuItems = menuItemRepository.findAll();

        // then
        assertEquals(3, menuItems.size());
        assertEquals(1, menuItems.get(0).getId());
        assertEquals("Chicken", menuItems.get(0).getName());
        assertEquals(10.99, menuItems.get(0).getPrice());
        assertEquals(2, menuItems.get(0).getMenu().getId());
        assertEquals("Food", menuItems.get(0).getMenu().getName());
        assertEquals(1, menuItems.get(0).getOrders().size());
        assertEquals(2, menuItems.get(1).getId());
        assertEquals("Coke", menuItems.get(1).getName());
        assertEquals(1.99, menuItems.get(1).getPrice());
        assertEquals(1, menuItems.get(1).getMenu().getId());
        assertEquals("Drinks", menuItems.get(1).getMenu().getName());
        assertEquals(2, menuItems.get(1).getOrders().size());
        assertEquals(3, menuItems.get(2).getId());
        assertEquals("Tiramisu", menuItems.get(2).getName());
        assertEquals(5.99, menuItems.get(2).getPrice());
        assertEquals(2, menuItems.get(2).getMenu().getId());
        assertEquals("Food", menuItems.get(2).getMenu().getName());
        assertEquals(1, menuItems.get(2).getOrders().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneMenuItem() {
        // when
        Optional<MenuItem> menuItem = menuItemRepository.findById(1L);

        // then
        assertNotNull(menuItem.get());
        assertEquals(1, menuItem.get().getId());
        assertEquals("Chicken", menuItem.get().getName());
        assertEquals(10.99, menuItem.get().getPrice());
        assertEquals(2, menuItem.get().getMenu().getId());
        assertEquals("Food", menuItem.get().getMenu().getName());
        assertEquals(1, menuItem.get().getOrders().size());
    }

/*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnHigherSizeOfList_whenCreateOne() {

    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuItemRepository.deleteById(2L);
        List<MenuItem> menuItems = menuItemRepository.findAll();

        // then
        assertEquals(2, menuItems.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnMenuItems_whenMenuNamePass() {
        // when
        List<MenuItem> menuItems = menuItemRepository.findByMenu(spy(new Menu(1L,"Drinks",List.of())));

        // then
        assertEquals(1, menuItems.size());
        assertEquals(2, menuItems.get(0).getId());
        assertEquals("Coke", menuItems.get(0).getName());
        assertEquals(1.99, menuItems.get(0).getPrice());
        assertEquals(1, menuItems.get(0).getMenu().getId());
        assertEquals("Drinks", menuItems.get(0).getMenu().getName());
        assertEquals(2, menuItems.get(0).getOrders().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnEmptyList_whenWrongMenuNamePass() {
        // when
        List<MenuItem> menuItems = menuItemRepository.findByMenu(spy(new Menu(3L,"Random",List.of())));

        // then
        assertEquals(0, menuItems.size());
    }
}
