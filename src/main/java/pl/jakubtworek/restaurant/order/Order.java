package pl.jakubtworek.restaurant.order;

import pl.jakubtworek.restaurant.auth.User;
import pl.jakubtworek.restaurant.employee.Employee;
import pl.jakubtworek.restaurant.menu.MenuItem;

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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "orders")
public class Order {

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
    private List<MenuItem> menuItems;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "Order_Employee",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Order() {
    }

    public Order(final OrderDto source) {
        this.id = source.getId();
        this.price = source.getPrice();
        this.hourOrder = source.getHourOrder();
        this.hourAway = source.getHourAway();
        this.typeOfOrder = source.getTypeOfOrder();
        this.menuItems = source.getMenuItems().stream().map(MenuItem::new).collect(Collectors.toList());
        this.employees = source.getEmployees().stream().map(Employee::new).collect(Collectors.toList());
        this.user = new User(source.getUserDto());
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    int getPrice() {
        return price;
    }

    void setPrice(final int price) {
        this.price = price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    void setHourOrder(final ZonedDateTime hourOrder) {
        this.hourOrder = hourOrder;
    }

    ZonedDateTime getHourAway() {
        return hourAway;
    }

    void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }

    TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    void setTypeOfOrder(final TypeOfOrder typeOfOrder) {
        this.typeOfOrder = typeOfOrder;
    }

    List<MenuItem> getMenuItems() {
        return menuItems;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    void setEmployees(final List<Employee> employees) {
        this.employees = employees;
    }

    User getUser() {
        return user;
    }

    void setUser(final User user) {
        this.user = user;
    }
}