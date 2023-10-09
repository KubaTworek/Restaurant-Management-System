package pl.jakubtworek.order;

import pl.jakubtworek.auth.SqlSimpleUser;
import pl.jakubtworek.employee.SqlSimpleEmployee;
import pl.jakubtworek.menu.SqlSimpleMenuItem;
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
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
class SqlOrder {
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

    @ManyToMany(cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name = "Order_Menu_Item",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "menu_item_id")
    )
    private List<SqlSimpleMenuItem> menuItems;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "Order_Employee",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<SqlSimpleEmployee> employees;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
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
        result.menuItems = source.getMenuItems().stream().map(SqlSimpleMenuItem::fromMenuItem).collect(Collectors.toList());
        result.employees = source.getEmployees().stream().map(SqlSimpleEmployee::fromEmployee).collect(Collectors.toList());
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
