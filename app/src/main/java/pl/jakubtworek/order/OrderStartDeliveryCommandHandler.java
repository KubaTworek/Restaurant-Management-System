package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.order.command.OrderStartDeliveryCommand;

class OrderStartDeliveryCommandHandler implements CommandHandler<OrderStartDeliveryCommand> {
    private final Order order;

    OrderStartDeliveryCommandHandler(final Order order) {
        this.order = order;
    }

    @Override
    public void handle(OrderStartDeliveryCommand command) {
        order.startDelivery(command.orderId());
    }
}