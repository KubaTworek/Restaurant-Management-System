package pl.jakubtworek.order;

import pl.jakubtworek.auth.SqlSimpleUser;
import pl.jakubtworek.employee.SqlSimpleEmployee;
import pl.jakubtworek.menu.SqlSimpleMenuItem;
import pl.jakubtworek.order.dto.TypeOfOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.time.ZonedDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "ORDERS")
class SqlOrder {
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

    @ManyToMany
    @JoinTable(
            name = "ORDERS__MENU_ITEMS",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "MENU_ITEM_ID")
    )
    private Set<SqlSimpleMenuItem> menuItems;

    @ManyToMany
    @JoinTable(
            name = "ORDERS__EMPLOYEE",
            joinColumns = @JoinColumn(name = "ORDER_ID"),
            inverseJoinColumns = @JoinColumn(name = "EMPLOYEE_ID")
    )
    private Set<SqlSimpleEmployee> employees;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private SqlSimpleUser user;

    public SqlOrder() {
    }

    static SqlOrder fromOrder(Order source) {
        SqlOrder result = new SqlOrder();
        result.id = source.getId();
        result.price = source.getPrice();
        result.hourOrder = source.getHourOrder();
        result.hourAway = source.getHourAway();
        result.typeOfOrder = source.getTypeOfOrder();
        result.menuItems = source.getMenuItems().stream().map(SqlSimpleMenuItem::fromMenuItem).collect(Collectors.toSet());
        result.employees = source.getEmployees().stream().map(SqlSimpleEmployee::fromEmployee).collect(Collectors.toSet());
        result.user = SqlSimpleUser.fromUser(source.getUser());
        return result;
    }

    Order toOrder() {
        Order result = new Order();
        result.setId(id);
        result.setPrice(price);
        result.setHourOrder(hourOrder);
        result.setHourAway(hourAway);
        result.setTypeOfOrder(typeOfOrder);
        result.setMenuItems(menuItems.stream().map(SqlSimpleMenuItem::toMenuitem).collect(Collectors.toList()));
        result.setEmployees(employees.stream().map(SqlSimpleEmployee::toEmployee).collect(Collectors.toList()));
        result.setUser(user.toUser());
        return result;
    }
}
