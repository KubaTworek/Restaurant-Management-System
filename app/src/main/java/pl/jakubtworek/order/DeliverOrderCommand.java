package pl.jakubtworek.order;

import pl.jakubtworek.common.Command;

public class DeliverOrderCommand implements Command {
    private final Long orderId;

    public DeliverOrderCommand(Long orderId) {
        this.orderId = orderId;
    }

    public Long getOrderId() {
        return orderId;
    }
}
