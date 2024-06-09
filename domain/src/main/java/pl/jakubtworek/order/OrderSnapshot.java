package pl.jakubtworek.order;

import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.vo.OrderStatus;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class OrderSnapshot {
    private Long id;
    private OrderPriceSnapshot price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourPrepared;
    private ZonedDateTime hourReceived;
    private TypeOfOrder typeOfOrder;
    private OrderStatus status;
    private OrderDeliverySnapshot delivery;
    private Set<OrderItemSnapshot> orderItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private CustomerId customerId;

    OrderSnapshot() {
    }

    OrderSnapshot(final Long id,
                  final OrderPriceSnapshot price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourPrepared,
                  final ZonedDateTime hourReceived,
                  final TypeOfOrder typeOfOrder,
                  final OrderStatus status,
                  final OrderDeliverySnapshot delivery,
                  final Set<OrderItemSnapshot> orderItems,
                  final Set<EmployeeId> employees,
                  final CustomerId customerId
    ) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourPrepared = hourPrepared;
        this.hourReceived = hourReceived;
        this.typeOfOrder = typeOfOrder;
        this.status = status;
        this.delivery = delivery;
        this.orderItems = orderItems;
        this.employees = employees;
        this.customerId = customerId;
    }

    Long getId() {
        return id;
    }

    OrderPriceSnapshot getPrice() {
        return price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    ZonedDateTime getHourPrepared() {
        return hourPrepared;
    }

    ZonedDateTime getHourReceived() {
        return hourReceived;
    }

    TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    OrderStatus getStatus() {
        return status;
    }

    OrderDeliverySnapshot getDelivery() {
        return delivery;
    }

    Set<OrderItemSnapshot> getOrderItems() {
        return orderItems;
    }

    Set<EmployeeId> getEmployees() {
        return employees;
    }

    CustomerId getClientId() {
        return customerId;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final OrderSnapshot that = (OrderSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(price, that.price) && Objects.equals(hourOrder, that.hourOrder) && Objects.equals(hourPrepared, that.hourPrepared) && typeOfOrder == that.typeOfOrder && Objects.equals(orderItems, that.orderItems) && Objects.equals(employees, that.employees) && Objects.equals(customerId, that.customerId);
    }
}
