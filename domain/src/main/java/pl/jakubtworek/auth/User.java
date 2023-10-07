package pl.jakubtworek.auth;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.List;

class User {
    private Long id;
    private String username;
    private String password;
    private List<SimpleOrder> orders;

    public User() {
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getUsername() {
        return username;
    }

    void setUsername(final String username) {
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    void setPassword(final String password) {
        this.password = password;
    }

    List<SimpleOrder> getOrders() {
        return orders;
    }

    void setOrders(final List<SimpleOrder> orders) {
        this.orders = orders;
    }
}
