package pl.jakubtworek.restaurant.order;

import pl.jakubtworek.restaurant.auth.query.SimpleUserQueryDto;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;
import pl.jakubtworek.restaurant.menu.query.SimpleMenuItemQueryDto;
import pl.jakubtworek.restaurant.order.query.TypeOfOrder;

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
    private List<SimpleMenuItemQueryDto> menuItems;

    @ManyToMany(cascade = {CascadeType.DETACH})
    @JoinTable(
            name = "Order_Employee",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "employee_id")
    )
    private List<SimpleEmployeeQueryDto> employees = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private SimpleUserQueryDto user;

    public Order() {
    }

    void setId(final Long id) {
        this.id = id;
    }

    void setPrice(final int price) {
        this.price = price;
    }

    void setHourOrder(final ZonedDateTime hourOrder) {
        this.hourOrder = hourOrder;
    }

    void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }

    void setTypeOfOrder(final TypeOfOrder typeOfOrder) {
        this.typeOfOrder = typeOfOrder;
    }

    List<SimpleMenuItemQueryDto> getMenuItems() {
        return menuItems;
    }

    void setUser(final SimpleUserQueryDto user) {
        this.user = user;
    }

    void add(SimpleEmployeeQueryDto employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }
}