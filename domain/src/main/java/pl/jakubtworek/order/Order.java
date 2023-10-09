package pl.jakubtworek.order;

import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

class Order {
    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private List<SimpleMenuItem> menuItems = new ArrayList<>();
    private List<SimpleEmployee> employees = new ArrayList<>();
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