package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.dto.OrderRequest;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class OrderFactory {
    private final UserFacade userFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderRepository orderRepository;

    public OrderFactory(UserFacade userFacade, final MenuItemFacade menuItemFacade, OrderRepository orderRepository) {
        this.userFacade = userFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderRepository = orderRepository;
    }

    public Order createOrder(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var menuItems = getMenuItems(toSave.getMenuItems());

        final var order = new Order();
        order.updateInfo(
                menuItems.stream().map(MenuItemDto::getId).map(MenuItemId::new).collect(Collectors.toSet()),
                calculatePrice(menuItems),
                toSave.getTypeOfOrder(),
                new UserId(user.getId())
        );

        return orderRepository.save(order);
    }


    private Set<MenuItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .collect(Collectors.toSet());
    }

    private int calculatePrice(Set<MenuItemDto> menuItems) {
        return menuItems.stream()
                .mapToInt(MenuItemDto::getPrice)
                .sum();
    }
}
