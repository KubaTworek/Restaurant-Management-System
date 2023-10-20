package pl.jakubtworek.menu;

import pl.jakubtworek.order.SqlSimpleOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "MENU_ITEMS")
class SqlMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ITEM_ID")
    private Long id;

    @Column(name = "MENU_ITEM_NAME")
    private String name;

    @Column(name = "PRICE")
    private int price;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    private SqlMenu menu;

    @ManyToMany(mappedBy = "menuItems")
    private Set<SqlSimpleOrder> orders;

    public SqlMenuItem() {
    }

    static SqlMenuItem fromMenuItem(MenuItem source) {
        SqlMenuItem result = new SqlMenuItem();
        result.id = source.getId();
        result.name = source.getName();
        result.price = source.getPrice();
        result.menu = SqlMenu.fromMenu(source.getMenu());
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toSet());
        return result;
    }

    MenuItem toMenuItem() {
        MenuItem result = new MenuItem();
        result.setId(id);
        result.setName(name);
        result.setPrice(price);
        result.setMenu(menu.toMenu());
        result.setOrders(orders.stream().map(SqlSimpleOrder::toOrder).collect(Collectors.toList()));
        return result;
    }
}
