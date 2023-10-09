package pl.jakubtworek.auth;

import pl.jakubtworek.order.SqlSimpleOrder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
class SqlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username")
    private String username;

    @Column(name = "password")
    private String password;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<SqlSimpleOrder> orders = new ArrayList<>();

    public SqlUser() {
    }

    static SqlUser fromUser(User source) {
        SqlUser result = new SqlUser();
        result.id = source.getId();
        result.username = source.getUsername();
        result.password = source.getPassword();
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toList());
        return result;
    }

    User toUser() {
        User result = new User();
        result.setId(id);
        result.setUsername(username);
        result.setPassword(password);
        result.setOrders(orders.stream().map(SqlSimpleOrder::toOrder).collect(Collectors.toList()));
        return result;
    }
}
