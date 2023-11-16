package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderRequest;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

class OrderFactory {
    private final UserFacade userFacade;
    private final MenuItemFacade menuItemFacade;

    OrderFactory(final UserFacade userFacade,
                 final MenuItemFacade menuItemFacade
    ) {
        this.userFacade = userFacade;
        this.menuItemFacade = menuItemFacade;
    }

    Order createOrder(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var menuItems = getMenuItems(toSave.menuItems());
        final var itemAmountMap = calculateAmount(menuItems);

        final var order = new Order();
        order.updateInfo(itemAmountMap.entrySet().stream()
                .map(entry -> new OrderItem(
                        null, entry.getKey(), calculatePriceForItem(entry.getKey(), menuItems), entry.getValue(), order
                ))
                .collect(Collectors.toSet()),
                new Money(calculatePrice(menuItems)),
                toSave.typeOfOrder(),
                new UserId(user.getId())
        );

        return order;
    }

    private Map<String, Integer> calculateAmount(List<MenuItemDto> menuItems) {
        Map<String, Integer> itemAmountMap = new HashMap<>();

        for (MenuItemDto menuItem : menuItems) {
            String itemName = menuItem.getName();

            if (itemAmountMap.containsKey(itemName)) {
                int currentAmount = itemAmountMap.get(itemName);
                itemAmountMap.put(itemName, currentAmount + 1);
            } else {
                itemAmountMap.put(itemName, 1);
            }
        }

        return itemAmountMap;
    }

    private Money calculatePriceForItem(String itemName, List<MenuItemDto> menuItems) {
        return menuItems.stream()
                .filter(mi -> mi.getName().equals(itemName))
                .findFirst()
                .map(mi -> new Money(mi.getPrice()))
                .orElse(new Money(new BigDecimal("0")));
    }

    private List<MenuItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .toList();
    }

    private BigDecimal calculatePrice(List<MenuItemDto> menuItems) {
        return menuItems.stream()
                .map(MenuItemDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
