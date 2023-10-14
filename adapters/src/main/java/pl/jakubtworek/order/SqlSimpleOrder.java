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
@Table(name = "ORDERS")
public class SqlSimpleOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ORDER_ID")
    private Long id;

    @Column(name = "PRICE")
    private int price;

    @Column(name = "HOUR_ORDER")
    private ZonedDateTime hourOrder;

    @Column(name = "HOUR_AWAY")
    private ZonedDateTime hourAway;

    @Column(name = "TYPE_OF_ORDER")
    private TypeOfOrder typeOfOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID")
    private SqlSimpleUser user;

    @ManyToMany
    @JoinTable(
            name = "ORDERS__EMPLOYEE",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID")
    )
    private List<SqlSimpleEmployee> employees;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "ORDERS__MENU_ITEMS",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ITEM_ID")
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
