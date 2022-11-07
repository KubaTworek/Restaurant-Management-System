package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import static org.junit.jupiter.api.Assertions.assertEquals;


public class MenuFactoryTest {
    private MenuFactory menuFactory;

    @BeforeEach
    public void setUp() {
        menuFactory = new MenuFactory();
    }

    @Test
    public void shouldReturnCook_whenProvideCook(){
        // given
        GetMenuDTO menuDTO = new GetMenuDTO(1L, "Food");

        // when
        Menu menu = menuFactory.createMenu(menuDTO);

        // then
        assertEquals("Food", menu.getName());
    }
}
