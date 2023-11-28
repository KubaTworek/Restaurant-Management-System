package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.common.vo.Money;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

class Order {
    private static final String ORDER_NOT_FOUND_ERROR = "Order with that id doesn't exist";

    private Long id;
    private Money price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private Set<OrderItem> orderItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private CustomerId user;
    private DomainEventPublisher publisher;
    private OrderRepository repository;

    Order() {
    }

    private Order(final Long id,
                  final Money price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourAway,
                  final TypeOfOrder typeOfOrder,
                  final Set<OrderItem> orderItems,
                  final Set<EmployeeId> employees,
                  final CustomerId user
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
                    price != null ? price.value() : null,
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
                price != null ? price.value() : null,
                hourOrder,
                hourAway,
                typeOfOrder,
                orderItems.stream().map(oi -> oi.getSnapshot(depth - 1)).collect(Collectors.toSet()),
                employees,
                user
        );
    }

    void setDependencies(DomainEventPublisher publisher, OrderRepository repository) {
        this.publisher = publisher;
        this.repository = repository;
    }

    Order from(List<ItemDto> items, String orderType, CustomerId customerId) {
        this.orderItems = OrderItemFactory.from(items);
        this.orderItems.forEach(oi -> oi.setOrder(this));
        this.price = new Money(calculateTotalPrice());
        this.hourOrder = ZonedDateTime.now();
        this.typeOfOrder = getAndValidateTypeOfOrder(orderType);
        this.user = customerId;
        final var created = this.repository.save(this);
        this.publisher.publish(
                new OrderEvent(
                        created.id, null, this.typeOfOrder, this.orderItems.size(), OrderEvent.State.TODO
                )
        );
        return created;
    }

    void addEmployee(Long orderId, EmployeeId employee) {
        if (employee != null) {
            final var order = this.getById(orderId);
            order.employees.add(employee);
            this.repository.save(order);
        }
    }

    void setAsDelivered(Long orderId) {
        final var order = this.getById(orderId);
        order.hourAway = ZonedDateTime.now();
        this.repository.save(order);
    }

    private Order getById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new IllegalStateException(ORDER_NOT_FOUND_ERROR));
    }

    private BigDecimal calculateTotalPrice() {
        return orderItems.stream()
                .map(OrderItem::calculatePrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private TypeOfOrder getAndValidateTypeOfOrder(String typeOfOrder) {
        try {
            return TypeOfOrder.valueOf(typeOfOrder);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid type of order type!!");
        }
    }
}
