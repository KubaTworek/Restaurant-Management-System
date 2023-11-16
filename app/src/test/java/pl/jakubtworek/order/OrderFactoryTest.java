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
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
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
        MockitoAnnotations.openMocks(this);
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
        final var burger = MenuItemDto.create(1L, "Burger", new BigDecimal("10.00"), Status.ACTIVE);
        final var fries = MenuItemDto.create(2L, "Fries", new BigDecimal("5.00"), Status.ACTIVE);

        when(menuItemFacade.getByName(eq("Burger"))).thenReturn(burger);
        when(menuItemFacade.getByName(eq("Fries"))).thenReturn(fries);
        when(userFacade.getByToken(jwt)).thenReturn(user);

        // when
        final var order = orderFactory.createOrder(orderRequest, jwt);

        // then
        final var snap = order.getSnapshot(1);
        assertEquals(user.getId(), snap.getClientId().getId());
        assertEquals(2, snap.getOrderItems().size());
        assertEquals(15.00, snap.getPrice().doubleValue());
        assertEquals(TypeOfOrder.TAKE_AWAY, snap.getTypeOfOrder());
    }
}
