package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

class OrderFactoryTest {
    @Mock
    private UserFacade userFacade;
    @Mock
    private MenuItemFacade menuItemFacade;

    private OrderFactory orderFactory;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderFactory = new OrderFactory(
                userFacade,
                menuItemFacade
        );
    }

    @Test
    void shouldCreateOrder() {
        // given
        final var orderRequest = new OrderRequest(
                "TAKE_AWAY", List.of("Burger", "Fries")
        );
        final var jwt = "exampleJwt";
        final var user = UserDto.create(
                1L, "username", "password"
        );
        final var burger = MenuItemDto.create(1L, "Burger", 10);
        final var fries = MenuItemDto.create(2L, "Fries", 5);

        when(menuItemFacade.getByName(eq("Burger"))).thenReturn(burger);
        when(menuItemFacade.getByName(eq("Fries"))).thenReturn(fries);
        when(userFacade.getByToken(jwt)).thenReturn(user);

        // when
        final var order = orderFactory.createOrder(orderRequest, jwt);

        // then
        final var snap = order.getSnapshot();
        assertEquals(user.getId(), snap.getUser().getId());
        assertEquals(2, snap.getMenuItems().size());
        assertEquals(15, snap.getPrice());
        assertEquals(TypeOfOrder.TAKE_AWAY, snap.getTypeOfOrder());
    }
}
