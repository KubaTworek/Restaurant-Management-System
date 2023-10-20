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
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "USERS")
class SqlUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USER_ID")
    private Long id;

    @Column(name = "USERNAME")
    private String username;

    @Column(name = "PASSWORD")
    private String password;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.REFRESH, CascadeType.DETACH})
    private Set<SqlSimpleOrder> orders = new HashSet<>();

    public SqlUser() {
    }

    static SqlUser fromUser(User source) {
        SqlUser result = new SqlUser();
        result.id = source.getId();
        result.username = source.getUsername();
        result.password = source.getPassword();
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toSet());
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
