package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.ItemDto;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class OrderItemFactory {
    static Set<OrderItem> from(List<ItemDto> menuItems) {
        final var itemAmountMap = calculateAmount(menuItems);

        return itemAmountMap.entrySet().stream()
                .map(entry -> createOrderItem(entry.getKey(), new HashSet<>(menuItems), entry.getValue()))
                .collect(Collectors.toSet());
    }

    private static OrderItem createOrderItem(String name, Set<ItemDto> menuItems, Integer amount) {
        final var item = new OrderItem();
        item.updateInfo(name, menuItems, amount);
        return item;
    }

    private static Map<String, Integer> calculateAmount(List<ItemDto> menuItems) {
        return menuItems.stream()
                .collect(Collectors.groupingBy(ItemDto::getName, Collectors.summingInt(e -> 1)));
    }
}
