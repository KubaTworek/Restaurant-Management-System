package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuItemServiceIT {

    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnAllMenuItems() {
        // when
        List<MenuItemDTO> menuItems = menuItemService.findAll();

        // then
        assertEquals(3, menuItems.size());

        assertEquals("Chicken", menuItems.get(0).getName());
        assertEquals(10.99, menuItems.get(0).getPrice());
        assertEquals("Food", menuItems.get(0).getMenu().getName());
        assertEquals(1, menuItems.get(0).getOrders().size());

        assertEquals("Coke", menuItems.get(1).getName());
        assertEquals(1.99, menuItems.get(1).getPrice());
        assertEquals("Drinks", menuItems.get(1).getMenu().getName());
        assertEquals(2, menuItems.get(1).getOrders().size());

        assertEquals("Tiramisu", menuItems.get(2).getName());
        assertEquals(5.99, menuItems.get(2).getPrice());
        assertEquals("Food", menuItems.get(2).getMenu().getName());
        assertEquals(1, menuItems.get(2).getOrders().size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnOneMenuItem() {
        // when
        Optional<MenuItemDTO> menuItem = menuItemService.findById(1L);

        // then
        assertEquals("Chicken", menuItem.get().getName());
        assertEquals(10.99, menuItem.get().getPrice());
        assertEquals("Food", menuItem.get().getMenu().getName());
        assertEquals(1, menuItem.get().getOrders().size());
    }

    @Test
    @Sql(statements = "INSERT INTO `menu`(`id`, `name`) VALUES (1, 'Drinks'), (2, 'Food')")
    public void shouldReturnCreatedMenuItem() {
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Pizza", 12.99, "Food");
        MenuDTO menuDTO = new MenuDTO(1L, "Food", List.of());

        // when
        MenuItemDTO menuItemReturned = menuItemService.save(menuItem, menuDTO);

        // then
        assertEquals("Pizza", menuItemReturned.getName());
        assertEquals(12.99, menuItemReturned.getPrice());
        assertEquals("Food", menuItemReturned.getMenu().getName());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuItemService.deleteById(2L);
        List<MenuItemDTO> menuItems = menuItemService.findAll();

        // then
        assertEquals(2, menuItems.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnMenuItems_whenMenuNamePass() {
        // when
        Menu menu = new Menu(1L, "Food", List.of());
        List<MenuItemDTO> menuItems = menuItemService.findByMenu(menu);

        // then
        assertEquals(1, menuItems.size());

        assertEquals("Coke", menuItems.get(0).getName());
        assertEquals(1.99, menuItems.get(0).getPrice());
        assertEquals("Drinks", menuItems.get(0).getMenu().getName());
        assertEquals(2, menuItems.get(0).getOrders().size());
    }
}
