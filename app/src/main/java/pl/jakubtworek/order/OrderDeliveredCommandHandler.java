package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.order.command.OrderDeliveredCommand;

class OrderDeliveredCommandHandler implements CommandHandler<OrderDeliveredCommand> {
    private final Order order;

    OrderDeliveredCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(OrderDeliveredCommand command) {
        order.delivery(command.orderId());
    }
}
