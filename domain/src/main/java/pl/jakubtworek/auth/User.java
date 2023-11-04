package pl.jakubtworek.auth;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class User {
    static User restore(UserSnapshot snapshot) {
        return new User(
                snapshot.getId(),
                snapshot.getUsername(),
                snapshot.getPassword(),
                snapshot.getOrders().stream().map(SimpleOrder::restore).collect(Collectors.toList())
        );
    }

    private Long id;
    private String username;
    private String password;
    private List<SimpleOrder> orders = new ArrayList<>();

    public User() {
    }

    private User(final Long id, final String username, final String password, final List<SimpleOrder> orders) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.orders = orders;
    }

    UserSnapshot getSnapshot() {
        return new UserSnapshot(
                id,
                username,
                password,
                orders.stream().map(SimpleOrder::getSnapshot).collect(Collectors.toSet())
        );
    }

    void updateInfo(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
