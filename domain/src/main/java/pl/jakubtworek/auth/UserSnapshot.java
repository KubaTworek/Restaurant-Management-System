package pl.jakubtworek.auth;

import pl.jakubtworek.order.dto.SimpleOrderSnapshot;

import java.util.HashSet;
import java.util.Set;

class UserSnapshot {
    private Long id;
    private String username;
    private String password;
    private Set<SimpleOrderSnapshot> orders = new HashSet<>();

    public UserSnapshot() {
    }

    UserSnapshot(final Long id, final String username, final String password, final Set<SimpleOrderSnapshot> orders) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.orders = orders;
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

    Set<SimpleOrderSnapshot> getOrders() {
        return orders;
    }
}
