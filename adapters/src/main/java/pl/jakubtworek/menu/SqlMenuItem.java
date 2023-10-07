package pl.jakubtworek.menu;

import pl.jakubtworek.order.SqlSimpleOrder;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "menu_item")
class SqlMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "menu_id")
    private SqlMenu menu;

    @ManyToMany
    @JoinTable(
            name = "Order_Menu_Item",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<SqlSimpleOrder> orders;

    public SqlMenuItem() {
    }

    static SqlMenuItem fromMenuItem(MenuItem source) {
        SqlMenuItem result = new SqlMenuItem();
        result.id = source.getId();
        result.name = source.getName();
        result.price = source.getPrice();
        result.menu = SqlMenu.fromMenu(source.getMenu());
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toList());
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
