package pl.jakubtworek.order;

import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

class OrderItemFactory {
    static Set<OrderItem> from(List<MenuItemDto> menuItems) {
        final var itemAmountMap = calculateAmount(menuItems);

        return itemAmountMap.entrySet().stream()
                .map(entry -> createOrderItem(entry.getKey(), calculatePriceForItem(entry.getKey(), menuItems), entry.getValue()))
                .collect(Collectors.toSet());
    }

    private static OrderItem createOrderItem(String name, Money price, Integer amount) {
        final var item = new OrderItem();
        item.updateInfo(name, price, amount);
        return item;
    }

    private static Map<String, Integer> calculateAmount(List<MenuItemDto> menuItems) {
        return menuItems.stream()
                .collect(Collectors.groupingBy(MenuItemDto::getName, Collectors.summingInt(e -> 1)));
    }

    private static Money calculatePriceForItem(String itemName, List<MenuItemDto> menuItems) {
        return menuItems.stream()
                .filter(mi -> mi.getName().equals(itemName))
                .findFirst()
                .map(mi -> new Money(mi.getPrice()))
                .orElse(new Money(BigDecimal.valueOf(0)));
    }
}
