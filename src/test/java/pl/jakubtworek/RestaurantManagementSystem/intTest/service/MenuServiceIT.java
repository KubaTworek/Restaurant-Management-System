package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.*;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuDTOAssertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenuRequest;

@SpringBootTest
@Transactional
class MenuServiceIT {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemService menuItemService;

    static private UUID idToDelete = UUID.randomUUID();

    @BeforeEach
    public void setup() throws MenuNotFoundException {


        MenuRequest menu1 = new MenuRequest("Drinks");
        MenuRequest menu2 = new MenuRequest("Food");
        idToDelete = menuService.save(menu1).getId();
        menuService.save(menu2);

        MenuItemRequest menuItem1 = new MenuItemRequest("Chicken", 10.99, "Food");
        MenuItemRequest menuItem2 = new MenuItemRequest("Coke", 1.99, "Drinks");
        MenuItemRequest menuItem3 = new MenuItemRequest("Tiramisu", 5.99, "Food");

        menuItemService.save(menuItem1);
        menuItemService.save(menuItem2);
        menuItemService.save(menuItem3);
    }

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
    void shouldReturnLowerSizeOfList_whenDeleteOne() throws MenuNotFoundException {
        // when
        MenuDTO menuDTO1 = menuService.findById(idToDelete).orElse(null);
        menuService.deleteById(idToDelete);
        MenuDTO menuDTO2 = menuService.findById(idToDelete).orElse(null);

        // then
        assertNotNull(menuDTO1);
        assertNull(menuDTO2);
    }

    @Test
    void shouldReturnAllMenu() {
        // when
        List<MenuDTO> menuListReturned = menuService.findAll();

        // then
        MenuDTOAssertions.checkAssertionsForMenus(menuListReturned);
    }

    @Test
    void shouldReturnOneMenu_whenPassId() {
        // when
        MenuDTO menuReturned = menuService.findById(UUID.fromString("340a81aa-7847-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }

    @Test
    void shouldReturnOneMenu_whenPassName() {
        // when
        MenuDTO menuReturned = menuService.findByName("Food").orElse(null);

        // then
        MenuDTOAssertions.checkAssertionsForMenu(menuReturned);
    }
}
