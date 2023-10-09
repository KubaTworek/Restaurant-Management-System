package pl.jakubtworek.order;

import pl.jakubtworek.auth.SqlSimpleUser;
import pl.jakubtworek.employee.SqlSimpleEmployee;
import pl.jakubtworek.menu.SqlSimpleMenuItem;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

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
import java.time.ZonedDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
public class SqlSimpleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private int price;

    @Column(name = "hour_order")
    private ZonedDateTime hourOrder;

    @Column(name = "hour_away")
    private ZonedDateTime hourAway;

    @Column(name = "type_of_order")
    private TypeOfOrder typeOfOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SqlSimpleUser user;

    @ManyToMany
    @JoinTable(
            name = "Order_Employee",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<SqlSimpleEmployee> employees;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "Order_Menu_Item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<SqlSimpleMenuItem> menuItems;

    public static SqlSimpleOrder fromOrder(SimpleOrder source) {
        SqlSimpleOrder result = new SqlSimpleOrder();
        result.id = source.getId();
        result.price = source.getPrice();
        result.hourOrder = source.getHourOrder();
        result.hourAway = source.getHourAway();
        result.typeOfOrder = source.getTypeOfOrder();
        return result;
    }

    public SimpleOrder toOrder() {
        return new SimpleOrder(id, price, hourOrder, hourAway, typeOfOrder);
    }
}
