package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.*;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuItemDTOAssertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createChickenMenuItemRequest;

@SpringBootTest
@Transactional
class MenuItemServiceIT {

    @Autowired
    private MenuService menuService;
    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private TypeOfOrderRepository typeOfOrderRepository;
    @Autowired
    private OrderRepository orderRepository;

    static private UUID idMenu;
    static private UUID idMenuItem;

    @BeforeEach
    public void setup() throws MenuNotFoundException {
        MenuRequest menu1 = new MenuRequest("Drinks");
        MenuRequest menu2 = new MenuRequest("Food");
        menuService.save(menu1);
        idMenu = menuService.save(menu2).getId();

        MenuItemRequest menuItemRequest1 = new MenuItemRequest("Chicken", 10.99, "Food");
        MenuItemRequest menuItemRequest2 = new MenuItemRequest("Coke", 1.99, "Drinks");
        MenuItemRequest menuItemRequest3 = new MenuItemRequest("Tiramisu", 5.99, "Food");

        MenuItem menuItem1 = menuItemService.save(menuItemRequest1).convertDTOToEntity();
        MenuItem menuItem2 = menuItemService.save(menuItemRequest2).convertDTOToEntity();
        MenuItem menuItem3 = menuItemService.save(menuItemRequest3).convertDTOToEntity();
        idMenuItem = menuItem1.getId();

        TypeOfOrder onsite = new TypeOfOrder(null, "On-site", List.of());
        TypeOfOrder delivery = new TypeOfOrder(null, "Delivery", List.of());
        typeOfOrderRepository.save(onsite);
        typeOfOrderRepository.save(delivery);

        Order onsiteOrder = new Order(null, 12.98, "2022-08-22", "12:00:00", "12:15:00", typeOfOrderRepository.findByType("On-site").get(), List.of(menuItem1, menuItem2), List.of(), null);
        Order deliveryOrder = new Order(null, 7.98, "2022-08-22", "12:05:00", null, typeOfOrderRepository.findByType("Delivery").get(), List.of(menuItem3, menuItem2), List.of(), null);

        orderRepository.save(onsiteOrder);
        orderRepository.save(deliveryOrder);
    }

    @Test
    void shouldReturnCreatedMenuItem() throws MenuNotFoundException {
        // given
        MenuItemRequest menuItem = createChickenMenuItemRequest();

        // when
        MenuItemDTO menuItemCreated = menuItemService.save(menuItem);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemCreated);
    }

    @Test
    void shouldReturnUpdatedMenuItem() throws MenuNotFoundException {
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Chicken Wings", 9.99, "Food");

        // when
        List<MenuDTO> menuDTOS1 = menuService.findAll();
        MenuItemDTO menuItemCreated = menuItemService.update(menuItem, idMenuItem);
        List<MenuDTO> menuDTOS2 = menuService.findAll();

        // then
        assertEquals(idMenuItem, menuItemCreated.getId());
        assertEquals("Chicken Wings", menuItemCreated.getName());
        assertEquals(9.99, menuItemCreated.getPrice());
        assertEquals("Food", menuItemCreated.getMenu().getName());
        assertEquals(2, menuDTOS1.size());
        assertEquals(2, menuDTOS2.size());
    }

    @Test
    void shouldDeleteMenuItem() throws MenuItemNotFoundException {
        // when
        MenuItemDTO menuItemDTO1 = menuItemService.findById(idMenuItem).orElse(null);
        menuItemService.deleteById(idMenuItem);
        MenuItemDTO menuItemDTO2 = menuItemService.findById(idMenuItem).orElse(null);

        // then
        assertNotNull(menuItemDTO1);
        assertNull(menuItemDTO2);
    }

    @Test
    void shouldNotDeleteMenuAndOrder_whenDeleteMenuItem() throws MenuItemNotFoundException {
        // when
        long ordersBeforeDelete = orderRepository.count();
        menuItemService.deleteById(idMenuItem);
        MenuDTO menu = menuService.findById(idMenu).orElse(null);
        long ordersAfterDelete = orderRepository.count();

        // then
        assertNotNull(menu);
        assertEquals(1, menu.getMenuItems().size());
        assertEquals(2, ordersBeforeDelete);
        assertEquals(2, ordersAfterDelete);
    }

    @Test
    void shouldReturnMenuItem_whenPassId() {
        // when
        MenuItemDTO menuItemReturned = menuItemService.findById(idMenuItem).orElse(null);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnMenuItems_whenPassMenuName() throws MenuNotFoundException {
        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu("Food");

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItemsInMenu(menuItemsReturned);
    }
}
