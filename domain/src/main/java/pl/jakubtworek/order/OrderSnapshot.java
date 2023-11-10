package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.menu.vo.MenuItemId;
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
    private Set<MenuItemId> menuItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private UserId user;

    public OrderSnapshot() {
    }

    OrderSnapshot(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, final Set<MenuItemId> menuItems, final Set<EmployeeId> employees, final UserId user) {
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

    Set<MenuItemId> getMenuItems() {
        return menuItems;
    }

    Set<EmployeeId> getEmployees() {
        return employees;
    }

    UserId getUser() {
        return user;
    }
}
