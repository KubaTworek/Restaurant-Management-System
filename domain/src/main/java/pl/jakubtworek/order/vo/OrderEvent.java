package pl.jakubtworek.order.vo;

import pl.jakubtworek.DomainEvent;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.Instant;

public class OrderEvent implements DomainEvent {

    private final Instant occurredOn;
    private final Long orderId;
    private final Long employeeId;
    private final TypeOfOrder orderType;
    private final Integer amountOfMenuItems;
    private final State state;
    public OrderEvent(final Long orderId, final Long employeeId, final TypeOfOrder orderType, final Integer amountOfMenuItems, final State state) {
        this.occurredOn = Instant.now();
        this.orderId = orderId;
        this.employeeId = employeeId;
        this.orderType = orderType;
        this.amountOfMenuItems = amountOfMenuItems;
        this.state = state;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public TypeOfOrder getOrderType() {
        return orderType;
    }

    public Integer getAmountOfMenuItems() {
        return amountOfMenuItems;
    }

    public State getState() {
        return state;
    }

    public enum State {
        TODO, READY, DELIVERED
    }
}
