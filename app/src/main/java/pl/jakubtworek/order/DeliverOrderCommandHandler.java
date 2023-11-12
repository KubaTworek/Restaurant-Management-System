package pl.jakubtworek.order;

import pl.jakubtworek.common.CommandHandler;
import pl.jakubtworek.order.delivery.DeliverOrderCommand;

class DeliverOrderCommandHandler implements CommandHandler<DeliverOrderCommand> {
    private final OrderFacade orderFacade;
    private final OrderRepository orderRepository;

    DeliverOrderCommandHandler(final OrderFacade orderFacade,
                                      final OrderRepository orderRepository
    ) {
        this.orderFacade = orderFacade;
        this.orderRepository = orderRepository;
    }

    @Override
    public void handle(DeliverOrderCommand command) {
        final var order = orderFacade.getById(command.getOrderId());
        order.delivery();
        orderRepository.save(order);
    }
}
