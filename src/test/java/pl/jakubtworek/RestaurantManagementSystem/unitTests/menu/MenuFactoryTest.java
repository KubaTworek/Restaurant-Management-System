package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenuRequest;

class MenuFactoryTest {
    private MenuFactory menuFactory;

    @BeforeEach
    void setUp() {
        menuFactory = new MenuFactory();
    }

    @Test
    void shouldReturnMenu(){
        // given
        MenuRequest menuDTO = createMenuRequest();

        // when
        MenuDTO menu = menuFactory.createMenu(menuDTO);

        // then
        assertEquals("Food", menu.getName());
    }
}
