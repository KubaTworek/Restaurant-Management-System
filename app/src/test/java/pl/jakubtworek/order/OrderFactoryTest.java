package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderFactoryTest {
    @Test
    void shouldCreateOrder() {
        // given
        final var burger = createMenuItemDto("Burger", BigDecimal.valueOf(10.00));
        final var fries = createMenuItemDto("Fries", BigDecimal.valueOf(5.00));

        // when
        final var order = OrderFactory.createOrder("TAKE_AWAY", 1L, List.of(burger, fries));

        // then
        final var result = order.getSnapshot(1);
        assertEquals(1L, result.getClientId().getId());
        assertEquals(2, result.getOrderItems().size());
        assertEquals(15.00, result.getPrice().doubleValue());
        assertEquals(TypeOfOrder.TAKE_AWAY, result.getTypeOfOrder());
    }

    private MenuItemDto createMenuItemDto(String name, BigDecimal price) {
        final var menuItemDto = mock(MenuItemDto.class);
        when(menuItemDto.getName()).thenReturn(name);
        when(menuItemDto.getPrice()).thenReturn(price);
        return menuItemDto;
    }
}
