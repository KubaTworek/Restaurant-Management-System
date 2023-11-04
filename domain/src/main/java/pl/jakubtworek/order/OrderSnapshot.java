package pl.jakubtworek.order;

import pl.jakubtworek.auth.dto.SimpleUserSnapshot;
import pl.jakubtworek.employee.dto.SimpleEmployeeSnapshot;
import pl.jakubtworek.menu.dto.SimpleMenuItemSnapshot;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

class OrderSnapshot {
    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private Set<SimpleMenuItemSnapshot> menuItems = new HashSet<>();
    private Set<SimpleEmployeeSnapshot> employees = new HashSet<>();
    private SimpleUserSnapshot user;

    public OrderSnapshot() {
    }

    OrderSnapshot(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, final Set<SimpleMenuItemSnapshot> menuItems, final Set<SimpleEmployeeSnapshot> employees, final SimpleUserSnapshot user) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.menuItems = menuItems;
        this.employees = employees;
        this.user = user;
    }

    Long getId() {
        return id;
    }

    int getPrice() {
        return price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    ZonedDateTime getHourAway() {
        return hourAway;
    }

    TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    Set<SimpleMenuItemSnapshot> getMenuItems() {
        return menuItems;
    }

    Set<SimpleEmployeeSnapshot> getEmployees() {
        return employees;
    }

    SimpleUserSnapshot getUser() {
        return user;
    }
}
