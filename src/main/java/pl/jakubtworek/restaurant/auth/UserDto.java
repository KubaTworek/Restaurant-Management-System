package pl.jakubtworek.restaurant.auth;

import pl.jakubtworek.restaurant.order.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserDto {
    private Long id;
    private String username;
    private String password;
    private List<OrderDto> orders;

    UserDto() {
    }

    public UserDto(final User source) {
        this.id = source.getId();
        this.username = source.getUsername();
        this.password = source.getPassword();
        this.orders = source.getOrders().stream().map(OrderDto::new).collect(Collectors.toList());
    }

    public void add(OrderDto order) {
        if (order != null) {
            orders.add(order);
            order.setUser(this);
        }
    }

    Long getId() {
        return id;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }

    List<OrderDto> getOrders() {
        return orders;
    }
}
