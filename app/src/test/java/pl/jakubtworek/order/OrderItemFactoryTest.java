package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class OrderItemFactoryTest {

    @Test
    void shouldCreateOrderItems() {
        // given
        final var menuItems = Arrays.asList(
                createMenuItemDto("Burger", BigDecimal.valueOf(15)),
                createMenuItemDto("Cola", BigDecimal.valueOf(5)),
                createMenuItemDto("Burger", BigDecimal.valueOf(15))
        );

        // when
        final var orderItems = OrderItemFactory.from(menuItems);

        // then
        assertEquals(2, orderItems.size());
        assertOrderItem(orderItems, "Burger", BigDecimal.valueOf(15), 2);
        assertOrderItem(orderItems, "Cola", BigDecimal.valueOf(5), 1);
    }

    private MenuItemDto createMenuItemDto(String name, BigDecimal price) {
        final var menuItemDto = mock(MenuItemDto.class);
        when(menuItemDto.getName()).thenReturn(name);
        when(menuItemDto.getPrice()).thenReturn(price);
        return menuItemDto;
    }

    private void assertOrderItem(Set<OrderItem> orderItems, String itemName, BigDecimal expectedPrice, int expectedAmount) {
        final var expectedItem = new OrderItem();
        expectedItem.updateInfo(itemName, new Money(expectedPrice), expectedAmount);

        assertTrue(orderItems.contains(expectedItem));
    }
}
