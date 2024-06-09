package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.order.command.OrderPreparedCommand;

class OrderPreparedCommandHandler implements CommandHandler<OrderPreparedCommand> {
    private final Order order;

    OrderPreparedCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(OrderPreparedCommand command) {
        order.prepare(command.orderId());
    }
}