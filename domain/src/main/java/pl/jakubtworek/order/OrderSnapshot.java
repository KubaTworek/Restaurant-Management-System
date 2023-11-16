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
    private Set<OrderItemSnapshot> orderItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private UserId clientId;

    public OrderSnapshot() {
    }

    OrderSnapshot(final Long id,
                  final BigDecimal price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourAway,
                  final TypeOfOrder typeOfOrder,
                  final Set<OrderItemSnapshot> orderItems,
                  final Set<EmployeeId> employees,
                  final UserId clientId
    ) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.orderItems = orderItems;
        this.employees = employees;
        this.clientId = clientId;
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

    Set<OrderItemSnapshot> getOrderItems() {
        return orderItems;
    }

    Set<EmployeeId> getEmployees() {
        return employees;
    }

    UserId getClientId() {
        return clientId;
    }
}
