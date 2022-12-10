package pl.jakubtworek.RestaurantManagementSystem.intTest.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.MenuNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.repository.MenuItemRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.MenuItemDTOAssertions;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class MenuItemServiceIT {

    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MenuItemRepository menuItemRepository;


    @Test
    void shouldReturnCreatedMenuItem() throws MenuNotFoundException {
        // given
        MenuItemRequest menuItem = new MenuItemRequest("Beef", 15.99, "Food");

        // when
        MenuItemDTO menuItemCreated = menuItemService.save(menuItem);
        List<MenuItem> meuItems = menuItemRepository.findAll();

        // then
        assertNotNull(menuItemCreated);

        assertEquals(4, meuItems.size());
    }

    @Test
    void shouldReturnOneMenuItem() {
        // when
        MenuItemDTO menuItemReturned = menuItemService.findById(UUID.fromString("46e3e96a-7847-11ed-a1eb-0242ac120002")).orElse(null);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    void shouldReturnMenuItems_whenMenuNamePass() throws MenuNotFoundException {
        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu("Food");

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItemsInMenu(menuItemsReturned);
    }
}
