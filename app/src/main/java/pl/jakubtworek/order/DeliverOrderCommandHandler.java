package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.order.command.DeliverOrderCommand;

class DeliverOrderCommandHandler implements CommandHandler<DeliverOrderCommand> {
    private final Order order;

    DeliverOrderCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(DeliverOrderCommand command) {
        order.delivery(command.orderId());
    }
}
