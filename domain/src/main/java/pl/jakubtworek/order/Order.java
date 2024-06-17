package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.vo.Address;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.OrderStatus;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

class Order {
    private static final String ORDER_NOT_FOUND_ERROR = "Order with that id doesn't exist";

    private Long id;
    private OrderPrice price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourPrepared;
    private ZonedDateTime hourReceived;
    private TypeOfOrder typeOfOrder;
    private OrderStatus status;
    private OrderDelivery delivery;
    private Set<OrderItem> orderItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private CustomerId user;
    private DomainEventPublisher publisher;
    private OrderRepository repository;
    private Long timeToWaitForReceive;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    Order() {
    }

    private Order(final Long id,
                  final OrderPrice price,
                  final ZonedDateTime hourOrder,
                  final ZonedDateTime hourPrepared,
                  final ZonedDateTime hourReceived,
                  final TypeOfOrder typeOfOrder,
                  final OrderStatus status,
                  final OrderDelivery delivery,
                  final Set<OrderItem> orderItems,
                  final Set<EmployeeId> employees,
                  final CustomerId user
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
        this.user = user;
    }

    static Order restore(OrderSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new Order(
                    snapshot.getId(),
                    OrderPrice.restore(snapshot.getPrice()),
                    snapshot.getHourOrder(),
                    snapshot.getHourPrepared(),
                    snapshot.getHourReceived(),
                    snapshot.getTypeOfOrder(),
                    snapshot.getStatus(),
                    snapshot.getDelivery() != null && snapshot.getTypeOfOrder().equals(TypeOfOrder.DELIVERY) ? OrderDelivery.restore(snapshot.getDelivery()) : null,
                    Collections.emptySet(),
                    snapshot.getEmployees(),
                    snapshot.getClientId()
            );
        }
        return new Order(
                snapshot.getId(),
                OrderPrice.restore(snapshot.getPrice()),
                snapshot.getHourOrder(),
                snapshot.getHourPrepared(),
                snapshot.getHourReceived(),
                snapshot.getTypeOfOrder(),
                snapshot.getStatus(),
                snapshot.getDelivery() != null && snapshot.getTypeOfOrder().equals(TypeOfOrder.DELIVERY) ? OrderDelivery.restore(snapshot.getDelivery()) : null,
                snapshot.getOrderItems().stream().map(oi -> OrderItem.restore(oi, depth - 1)).collect(Collectors.toSet()),
                snapshot.getEmployees(),
                snapshot.getClientId()
        );
    }

    OrderSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new OrderSnapshot(
                    id,
                    price.getSnapshot(),
                    hourOrder,
                    hourPrepared,
                    hourReceived,
                    typeOfOrder,
                    status,
                    null,
                    Collections.emptySet(),
                    employees,
                    user
            );
        }
        return new OrderSnapshot(
                id,
                price.getSnapshot(),
                hourOrder,
                hourPrepared,
                hourReceived,
                typeOfOrder,
                status,
                typeOfOrder == TypeOfOrder.DELIVERY ? delivery.getSnapshot() : null,
                orderItems.stream().map(oi -> oi.getSnapshot(depth - 1)).collect(Collectors.toSet()),
                employees,
                user
        );
    }

    void setDependencies(DomainEventPublisher publisher, OrderRepository repository, Long timeToWaitForReceive) {
        this.publisher = publisher;
        this.repository = repository;
        this.timeToWaitForReceive = timeToWaitForReceive;
    }

    Order create(List<ItemDto> items, String orderType, CustomerId customerId, Address address) {
        this.orderItems = OrderItemFactory.from(items);
        this.orderItems.forEach(oi -> oi.setOrder(this));
        this.price = OrderPriceFactory.from(calculateTotalPrice());
        this.hourOrder = ZonedDateTime.now();
        this.status = OrderStatus.NEW;
        this.typeOfOrder = getAndValidateTypeOfOrder(orderType);
        this.user = customerId;
        if (this.typeOfOrder.equals(TypeOfOrder.DELIVERY)) {
            this.delivery = OrderDeliveryFactory.from(address);
            this.price = OrderPriceFactory.from(calculateTotalPrice(), address);
        }
        return this.repository.save(this);
    }

    Order confirm(Long orderId, String decision, Long customerId) {
        final var order = this.getById(orderId);
        if (order.delivery == null && order.typeOfOrder.equals(TypeOfOrder.DELIVERY)) {
            order.delivery = this.delivery;
        }
        if (!order.status.equals(OrderStatus.NEW)) {
            throw new RuntimeException("Order should be in status NEW");
        }
        if (decision.equals("ACCEPT") && this.user.getId().equals(customerId)) {
            order.status = OrderStatus.ACCEPT;
            this.publisher.publish(
                    new OrderEvent(
                            order.id, null, order.typeOfOrder, order.orderItems.size(), order.delivery != null ? order.delivery.getDistrict() : null, OrderEvent.State.TODO
                    )
            );
        } else {
            order.status = OrderStatus.CANCELLED;
        }
        return this.repository.save(order);
    }

    void prepare(final Long orderId) {
        final var order = this.getById(orderId);
        if (!order.status.equals(OrderStatus.ACCEPT)) {
            throw new RuntimeException("Order should be in status ACCEPT");
        }
        order.hourPrepared = ZonedDateTime.now();
        if (order.typeOfOrder.equals(TypeOfOrder.DELIVERY)) {
            order.status = OrderStatus.READY;
            this.repository.save(order);
            publisher.publish(new OrderEvent(
                    order.id,
                    null,
                    order.typeOfOrder,
                    order.orderItems.size(),
                    order.delivery.getDistrict(),
                    OrderEvent.State.TO_DELIVER
            ));
        }
        if (order.typeOfOrder.equals(TypeOfOrder.ON_SITE)) {
            order.status = OrderStatus.TO_RECEIVE;
            this.repository.save(order);
            scheduleOrderStatusCheck(order.id);
        }
    }

    void startDelivery(final Long orderId) {
        final var order = this.getById(orderId);
        order.delivery.start();
        this.repository.save(order);
    }

    void delivery(Long orderId) {
        final var order = this.getById(orderId);
        if (!order.status.equals(OrderStatus.READY)) {
            throw new RuntimeException("Order should be in status READY");
        }
        order.delivery.end();
        order.status = OrderStatus.TO_RECEIVE;
        this.repository.save(order);
        scheduleOrderStatusCheck(order.id);
    }

    Order receive(Long orderId, BigDecimal tip, Long customerId) {
        final var order = this.getById(orderId);
        if (!order.status.equals(OrderStatus.TO_RECEIVE)) {
            throw new RuntimeException("Order should be in status TO_RECEIVE");
        }
        if (order.user.getId().equals(customerId)) {
            order.hourReceived = ZonedDateTime.now();
            order.status = OrderStatus.COMPLETED;
            if (tip != null) {
                order.price.addTip(tip);
            }
        }
        return this.repository.save(order);
    }

    void addEmployee(Long orderId, EmployeeId employee) {
        if (employee != null) {
            final var order = this.getById(orderId);
            order.employees.add(employee);
            this.repository.save(order);
        }
    }

    void addEmployeeToDelivery(final Long orderId, final EmployeeId employee) {
        if (employee != null) {
            final var order = this.getById(orderId);
            order.delivery.setEmployee(employee);
            this.repository.save(order);
        }
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

    private void scheduleOrderStatusCheck(Long orderId) {
        scheduler.schedule(() -> {
            final var order = getById(orderId);
            if (order.status == OrderStatus.TO_RECEIVE) {
                order.status = OrderStatus.CANCELLED;
                repository.save(order);
            }
        }, timeToWaitForReceive, TimeUnit.SECONDS);
    }
}
