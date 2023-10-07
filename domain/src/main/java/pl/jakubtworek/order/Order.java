package pl.jakubtworek.order;

import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
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
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
class Order {

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
    private List<SimpleMenuItem> menuItems;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "Order_Employee",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<SimpleEmployee> employees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SimpleUser user;

    public Order() {
    }

    List<SimpleMenuItem> getMenuItems() {
        return menuItems;
    }

    void setMenuItems(final List<SimpleMenuItem> menuItems) {
        this.menuItems = menuItems;
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

    List<SimpleEmployee> getEmployees() {
        return employees;
    }

    void setEmployees(final List<SimpleEmployee> employees) {
        this.employees = employees;
    }

    SimpleUser getUser() {
        return user;
    }

    void setUser(final SimpleUser user) {
        this.user = user;
    }

    void add(SimpleEmployee employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }
}