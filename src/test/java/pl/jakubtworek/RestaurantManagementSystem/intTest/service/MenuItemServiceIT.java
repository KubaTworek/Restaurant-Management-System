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
import pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

import java.util.List;

import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createChickenMenuItemRequest;

@SpringBootTest
@Transactional
class MenuItemServiceIT {

    @Autowired
    private MenuItemService menuItemService;
    @Autowired
    private MenuItemRepository menuItemRepository;

    @Test
    @Sql(statements = "INSERT INTO `menu`(`id`, `name`) VALUES (1, 'Food'), (2, 'Drinks')")
    void shouldReturnCreatedMenuItem() throws MenuNotFoundException {
        // given
        MenuItemRequest menuItem = createChickenMenuItemRequest();

        // when
        MenuItemDTO menuItemCreated = menuItemService.save(menuItem);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemCreated);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOneMenuItem() {
        // when
        MenuItemDTO menuItemReturned = menuItemService.findById(1L).orElse(null);

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItem(menuItemReturned);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnMenuItems_whenMenuNamePass() throws MenuNotFoundException {
        // when
        List<MenuItemDTO> menuItemsReturned = menuItemService.findByMenu("Food");

        // then
        MenuItemDTOAssertions.checkAssertionsForMenuItems(menuItemsReturned);
    }
}
