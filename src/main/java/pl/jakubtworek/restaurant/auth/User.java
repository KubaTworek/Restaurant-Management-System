package pl.jakubtworek.restaurant.auth;

import pl.jakubtworek.restaurant.order.Order;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<Order> orders;

    public User() {
    }

    public User(final UserDto source) {
        this.id = source.getId();
        this.username = source.getUsername();
        this.password = source.getPassword();
        this.orders = source.getOrders().stream().map(Order::new).collect(Collectors.toList());
    }

    Long getId() {
        return id;
    }

    public String getUsername() {
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

    List<Order> getOrders() {
        return orders;
    }
}
