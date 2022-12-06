package pl.jakubtworek.RestaurantManagementSystem.unitTests.menu;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.MenuFactory;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.createMenuRequest;

public class MenuFactoryTest {
    private MenuFactory menuFactory;

    @BeforeEach
    public void setUp() {
        menuFactory = new MenuFactory();
    }

    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        MenuRequest menuDTO = createMenuRequest();

        // when
        MenuDTO menu = menuFactory.createMenu(menuDTO);

        // then
        assertEquals("Drinks", menu.getName());
        assertNull(menu.getMenuItems());
    }
}
