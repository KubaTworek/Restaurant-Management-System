package pl.jakubtworek.order;

import org.junit.jupiter.api.Test;
import pl.jakubtworek.order.dto.ItemDto;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class OrderItemFactoryTest {

    @Test
    void shouldCalculatePriceForOrderItem() {
        // given
        final var items = createItems();

        // when
        final var orderItems = OrderItemFactory.from(items);

        // then
        assertEquals(2, orderItems.size());
        assertEquals(BigDecimal.valueOf(21.98), orderItems.stream().filter(oi -> "Burger".equals(oi.getSnapshot(0).getName())).findFirst().get().calculatePrice());
        assertEquals(BigDecimal.valueOf(12.99), orderItems.stream().filter(oi -> "Pizza".equals(oi.getSnapshot(0).getName())).findFirst().get().calculatePrice());
    }

    private List<ItemDto> createItems() {
        final List<ItemDto> items = new ArrayList<>();
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        items.add(new ItemDto("Pizza", BigDecimal.valueOf(12.99)));
        items.add(new ItemDto("Burger", BigDecimal.valueOf(10.99)));
        return items;
    }
}
