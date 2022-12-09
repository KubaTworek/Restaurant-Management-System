package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Transactional
class MenuItemServiceIT {

    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenuItem() {
        // when
        Optional<MenuItemDTO> menuItem = menuItemService.findById(1L);

        // then
        assertEquals("Chicken", menuItem.get().getName());
        assertEquals(10.99, menuItem.get().getPrice());
        assertEquals("Food", menuItem.get().getMenu().getName());
        assertEquals(1, menuItem.get().getOrders().size());
    }

    @Test
    @Sql(statements = "INSERT INTO `menu`(`id`, `name`) VALUES (1, 'Food'), (2, 'Drinks')")
    void shouldReturnCreatedMenuItem() throws MenuNotFoundException {
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Pizza", 12.99, "Food");

        // when
        MenuItemDTO menuItemReturned = menuItemService.save(menuItem);

        // then
        assertEquals("Pizza", menuItemReturned.getName());
        assertEquals(12.99, menuItemReturned.getPrice());
        assertEquals("Food", menuItemReturned.getMenu().getName());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnMenuItems_whenMenuNamePass() throws MenuNotFoundException {
        // when
        List<MenuItemDTO> menuItems = menuItemService.findByMenu("Food");

        // then
        assertEquals(1, menuItems.size());

        assertEquals("Coke", menuItems.get(0).getName());
        assertEquals(1.99, menuItems.get(0).getPrice());
        assertEquals("Drinks", menuItems.get(0).getMenu().getName());
        assertEquals(2, menuItems.get(0).getOrders().size());
    }
}
