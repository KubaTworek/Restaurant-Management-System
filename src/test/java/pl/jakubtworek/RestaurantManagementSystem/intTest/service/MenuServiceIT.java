package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.service.*;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuDTOAssertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

@SpringBootTest
@Transactional
@ActiveProfiles("test")
class MenuServiceIT {
    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemService menuItemService;

    static private UUID idMenu;
    static private UUID idMenuItem;

    @BeforeEach
    public void setup() throws MenuNotFoundException {
        MenuRequest menu1 = new MenuRequest("Drinks");
        MenuRequest menu2 = new MenuRequest("Food");
        menuService.save(menu1);
        idMenu = menuService.save(menu2).getId();

        MenuItemRequest menuItem1 = createChickenMenuItemRequest();
        MenuItemRequest menuItem2 = createCokeMenuItemRequest();
        MenuItemRequest menuItem3 = createTiramisuMenuItemRequest();

        menuItemService.save(menuItem1);
        menuItemService.save(menuItem2);
        idMenuItem = menuItemService.save(menuItem3).getId();
    }

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menu = new MenuRequest("Alcohol");

        // when
        MenuDTO menuCreated = menuService.save(menu);
        List<MenuDTO> menuDTOList = menuService.findAll();

        // then
        assertEquals("Alcohol", menuCreated.getName());
        assertEquals(3, menuDTOList.size());
    }

    @Test
    void shouldReturnUpdatedMenu() {
        // given
        MenuRequest menu = new MenuRequest("Alcohol");

        // when
        MenuDTO menuUpdated = menuService.update(menu, idMenu);

        // then
        assertEquals("Alcohol", menuUpdated.getName());
        assertEquals(2, menuUpdated.getMenuItems().size());
    }

    @Test
    void shouldDeleteMenu() throws MenuNotFoundException {
        // when
        MenuDTO menuDTO1 = menuService.findById(idMenu).orElse(null);
        menuService.deleteById(idMenu);
        MenuDTO menuDTO2 = menuService.findById(idMenu).orElse(null);

        // then
        assertNotNull(menuDTO1);
        assertNull(menuDTO2);
    }

    @Test
    void shouldDeleteMenuItems_whenDeleteMenu() throws MenuNotFoundException {
        // when
        menuService.deleteById(idMenu);
        MenuItemDTO menuItem = menuItemService.findById(idMenuItem).orElse(null);

        // then
        assertNull(menuItem);
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
        MenuDTO menuReturned = menuService.findById(idMenu).orElse(null);

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