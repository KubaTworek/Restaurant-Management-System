package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.spy;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenu;

@SpringBootTest
@Transactional
class MenuItemRepositoryIT {

    @Autowired
    private MenuItemRepository menuItemRepository;

    /*    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnCreatedMenuItem() {

    }*/

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        // when
        menuItemRepository.deleteById(2L);
        List<MenuItem> menuItems = menuItemRepository.findAll();

        // then
        assertEquals(2, menuItems.size());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllMenuItems() {
        // when
        List<MenuItem> menuItemsReturned = menuItemRepository.findAll();

        // then
        checkAssertionsForMenuItems(menuItemsReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenuItem() {
        // when
        MenuItem menuItemReturned = menuItemRepository.findById(1L).orElse(null);

        // then
        checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnMenuItems_whenMenuNamePass() {
        // when
        List<MenuItem> menuItemsReturned = menuItemRepository.findByMenu(createMenu());

        // then
        checkAssertionsForMenuItems(menuItemsReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    public void shouldReturnEmptyList_whenWrongMenuNamePass() {
        // when
        List<MenuItem> menuItems = menuItemRepository.findByMenu(spy(new Menu(3L,"Random",List.of())));

        // then
        assertEquals(0, menuItems.size());
    }

    private void checkAssertionsForMenuItem(MenuItem menuItem) {
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
    }

    private void checkAssertionsForMenuItems(List<MenuItem> menuItems) {
        assertEquals("Chicken", menuItems.get(0).getName());
        assertEquals(10.99, menuItems.get(0).getPrice());

        assertEquals("Tiramisu", menuItems.get(1).getName());
        assertEquals(5.99, menuItems.get(1).getPrice());
    }
}
