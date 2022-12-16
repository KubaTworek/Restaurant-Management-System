package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;

class MenuFactoryTest {
    private MenuFactory menuFactory;

    @BeforeEach
    void setUp() {
        menuFactory = new MenuFactory();
    }

    @Test
    void shouldReturnCreatedMenu() {
        // given
        MenuRequest menuDTO = createMenuRequest();

        // when
        MenuDTO menu = menuFactory.createMenu(menuDTO);

        // then
        assertEquals("Food", menu.getName());
    }

    @Test
    void shouldReturnUpdatedMenu() {
        // given
        MenuRequest newMenu = new MenuRequest("Alcohol");
        Menu oldMenu = createMenu();

        // when
        MenuDTO menu = menuFactory.updateMenu(newMenu, oldMenu);

        // then
        assertEquals("Alcohol", menu.getName());
    }
}