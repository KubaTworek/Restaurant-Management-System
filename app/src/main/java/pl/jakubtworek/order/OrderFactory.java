package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.dto.OrderRequest;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
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

        final var order = new Order();
        order.updateInfo(
                menuItems.stream().map(mi -> new OrderItem(
                        null, null, new MenuItemId(mi.getId())
                )).collect(Collectors.toSet()),
                new Money(calculatePrice(menuItems)),
                toSave.typeOfOrder(),
                new UserId(user.getId())
        );

        return order;
    }

    private Set<MenuItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .collect(Collectors.toSet());
    }

    private BigDecimal calculatePrice(Set<MenuItemDto> menuItems) {
        return menuItems.stream()
                .map(MenuItemDto::getPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
