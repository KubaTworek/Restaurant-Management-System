package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

class Order {
    private Long id;
    private Money price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private Set<OrderItem> orderItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private UserId user;

    public Order() {
    }

    private Order(final Long id,
                  final Money price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourAway,
                  final TypeOfOrder typeOfOrder,
                  final Set<OrderItem> orderItems,
                  final Set<EmployeeId> employees,
                  final UserId user
    ) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.orderItems = orderItems;
        this.employees = employees;
        this.user = user;
    }

    static Order restore(OrderSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new Order(
                    snapshot.getId(),
                    new Money(snapshot.getPrice()),
                    snapshot.getHourOrder(),
                    snapshot.getHourAway(),
                    snapshot.getTypeOfOrder(),
                    Collections.emptySet(),
                    snapshot.getEmployees(),
                    snapshot.getClientId()
            );
        }
        return new Order(
                snapshot.getId(),
                new Money(snapshot.getPrice()),
                snapshot.getHourOrder(),
                snapshot.getHourAway(),
                snapshot.getTypeOfOrder(),
                snapshot.getOrderItems().stream().map(oi -> OrderItem.restore(oi, depth - 1)).collect(Collectors.toSet()),
                snapshot.getEmployees(),
                snapshot.getClientId()
        );
    }

    OrderSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new OrderSnapshot(
                    id,
                    price != null ? price.getValue() : null,
                    hourOrder,
                    hourAway,
                    typeOfOrder,
                    Collections.emptySet(),
                    employees,
                    user
            );
        }
        return new OrderSnapshot(
                id,
                price != null ? price.getValue() : null,
                hourOrder,
                hourAway,
                typeOfOrder,
                orderItems.stream().map(oi -> oi.getSnapshot(depth - 1)).collect(Collectors.toSet()),
                employees,
                user
        );
    }

    void addEmployee(EmployeeId employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }

    void delivery() {
        this.hourAway = ZonedDateTime.now();
    }

    void updateInfo(Set<OrderItem> menuItems, Money price, String typeOfOrderName, UserId user) {
        this.orderItems = menuItems;
        this.price = price;
        this.hourOrder = ZonedDateTime.now();
        this.typeOfOrder = TypeOfOrder.valueOf(typeOfOrderName);
        this.user = user;
    }

    int getAmountOfMenuItems() {
        return this.orderItems.size();
    }
}
