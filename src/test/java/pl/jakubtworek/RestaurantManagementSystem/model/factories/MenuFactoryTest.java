package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;


public class MenuFactoryTest {
    private MenuFactory menuFactory;

    @BeforeEach
    public void setUp() {
        menuFactory = new MenuFactory();
    }

    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        MenuRequest menuDTO = new MenuRequest("Food");

        // when
        MenuDTO menu = menuFactory.createMenu(menuDTO);

        // then
        assertEquals("Food", menu.getName());
        assertNull(menu.getMenuItems());
    }
}
