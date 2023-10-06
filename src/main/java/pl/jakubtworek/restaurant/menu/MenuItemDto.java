package pl.jakubtworek.restaurant.menu;

import pl.jakubtworek.restaurant.order.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class MenuItemDto {
    private Long id;
    private String name;
    private int price;
    private MenuDto menu;
    private List<OrderDto> orders;

    MenuItemDto() {
    }

    public MenuItemDto(final MenuItem source) {
        this.id = source.getId();
        this.name = source.getName();
        this.price = source.getPrice();
        this.menu = new MenuDto(source.getMenu());
        this.orders = source.getOrders().stream().map(OrderDto::new).collect(Collectors.toList());
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    MenuDto getMenu() {
        return menu;
    }

    List<OrderDto> getOrders() {
        return orders;
    }
}
