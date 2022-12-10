package pl.jakubtworek.RestaurantManagementSystem.intTest.repository;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.spy;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

@SpringBootTest
@Transactional
class MenuItemRepositoryIT {

    @Autowired
    private MenuItemRepository menuItemRepository;
    @Autowired
    private MenuRepository menuRepository;

    @BeforeEach
    public void setup(){

    }

    /*    @Test
    @Sql({"/deleting-data.sql", "/data.sql"})
    void shouldReturnCreatedMenuItem() {

    }*/

    @Test
    void shouldReturnLowerSizeOfList_whenDeleteOne() {
        Menu menu = menuRepository.save(new Menu(null, "Food", List.of()));
        MenuItem menuItem = menuItemRepository.save(new MenuItem(null, "Chicken", 10.99, menu, List.of()));
        List<MenuItem> menuItems1 = menuItemRepository.findAll();

        UUID uuid = menuItem.getId();
        System.out.println(uuid);
        // when
        MenuItem menuItem1 = menuItemRepository.findById(uuid).get();
        menuItemRepository.deleteById(uuid);
        //List<MenuItem> menuItems2 = menuItemRepository.findAll();
        MenuItem menuItem2 = menuItemRepository.findById(uuid).get();

        // then
        assertEquals(1, menuItems1.size());
        //assertEquals(0, menuItems2.size());
    }

    @Test
    void shouldReturnAllMenuItems() {
        // when
        List<MenuItem> menuItemsReturned = menuItemRepository.findAll();

        for(MenuItem m : menuItemsReturned){
            System.out.println(m.getId());
        }

        // then
        MenuItemAssertions.checkAssertionsForMenuItems(menuItemsReturned);
    }

    @Test
    void shouldReturnOneMenuItem() {
        // when
        MenuItem menuItemReturned = menuItemRepository.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        MenuItemAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    public void shouldReturnMenuItems_whenMenuNamePass() {
        // when
        List<MenuItem> menuItemsReturned = menuItemRepository.findByMenu(createMenu());

        // then
        MenuItemAssertions.checkAssertionsForMenuItemsInMenu(menuItemsReturned);
    }

    @Test
    public void shouldReturnEmptyList_whenWrongMenuNamePass() {
        // when
        List<MenuItem> menuItems = menuItemRepository.findByMenu(spy(new Menu(UUID.fromString("b41874c8-784d-11ed-a1eb-0242ac120002"),"Random",List.of())));

        // then
        assertEquals(0, menuItems.size());
    }
}
