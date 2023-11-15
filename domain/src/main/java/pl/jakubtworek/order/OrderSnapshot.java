package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

class OrderSnapshot {
    private Long id;
    private BigDecimal price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private Set<OrderItem> menuItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private UserId user;

    public OrderSnapshot() {
    }

    OrderSnapshot(final Long id,
                  final BigDecimal price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourAway,
                  final TypeOfOrder typeOfOrder,
                  final Set<OrderItem> menuItems,
                  final Set<EmployeeId> employees,
                  final UserId user
    ) {
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

    BigDecimal getPrice() {
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

    Set<OrderItem> getMenuItems() {
        return menuItems;
    }

    Set<EmployeeId> getEmployees() {
        return employees;
    }

    UserId getUser() {
        return user;
    }
}
