package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.GetEmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import java.awt.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class MenuItemFactoryTest {
    @Mock
    private MenuService menuService;

    private MenuItemFactory menuItemFactory;

    @BeforeEach
    public void setUp() {
        menuService = mock(MenuService.class);

        menuItemFactory = new MenuItemFactory(
                menuService
                );

        Optional<Menu> foodMenu = Optional.of(new Menu(1L, "Food", null));

        when(menuService.findByName("Food")).thenReturn(foodMenu);
    }

    @Test
    public void shouldReturnMenuItem() {
        // given
        GetMenuItemDTO menuItemDTO = new GetMenuItemDTO(1L, "Apple", 1.99, "Food");

        // when
        MenuItem menuItem = menuItemFactory.createMenuItem(menuItemDTO);

        // then
        assertEquals("Apple", menuItem.getName());
        assertEquals(1.99, menuItem.getPrice());
        assertEquals("Food", menuItem.getMenu().getName());
        assertNull(menuItem.getOrders());
    }
}
