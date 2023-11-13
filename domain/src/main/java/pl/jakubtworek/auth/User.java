package pl.jakubtworek.auth;

import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Set;

class User {
    private Long id;
    private String username;
    private String password;
    private Set<OrderId> orders = new HashSet<>();

    public User() {
    }

    private User(final Long id,
                 final String username,
                 final String password,
                 final Set<OrderId> orders
    ) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.orders = orders;
    }

    static User restore(UserSnapshot snapshot) {
        return new User(
                snapshot.getId(),
                snapshot.getUsername(),
                snapshot.getPassword(),
                snapshot.getOrders()
        );
    }

    UserSnapshot getSnapshot() {
        return new UserSnapshot(
                id,
                username,
                password,
                orders
        );
    }

    void updateInfo(final String username, final String password) {
        this.username = username;
        this.password = password;
    }
}
