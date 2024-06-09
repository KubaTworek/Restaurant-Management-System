package pl.jakubtworek.order.delivery;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.common.CommandBus;
import pl.jakubtworek.order.command.AddEmployeeToDeliveryCommand;
import pl.jakubtworek.order.command.AddEmployeeToOrderCommand;
import pl.jakubtworek.order.command.OrderDeliveredCommand;
import pl.jakubtworek.order.command.OrderPreparedCommand;
import pl.jakubtworek.order.command.OrderStartDeliveryCommand;
import pl.jakubtworek.order.vo.OrderEvent;

@Service
class OrderEventListener {
    private static final Logger logger = LoggerFactory.getLogger(OrderEventListener.class);

    private final Kitchen kitchen;
    private final DeliveryHandler deliveryHandler;
    private final CommandBus commandBus;

    OrderEventListener(final Kitchen kitchen,
                       final DeliveryHandler deliveryHandler,
                       final CommandBus commandBus
    ) {
        this.kitchen = kitchen;
        this.deliveryHandler = deliveryHandler;
        this.commandBus = commandBus;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        logger.info("Handling order event: {}", event);

        switch (event.getState()) {
            case TODO -> handleTodo(event);
            case READY -> handleReady(event);
            case START_DELIVERY -> handleStartDelivery(event);
            case TO_DELIVER -> handleToDeliver(event);
            case DELIVERED -> handleDelivered(event);
            default -> logger.warn("Unknown state: {}", event.getState());
        }
    }

    private void handleStartDelivery(final OrderEvent event) {
        logger.info("Handling START_DELIVERY event for order: {}", event.getOrderId());
        commandBus.dispatch(new OrderStartDeliveryCommand(event.getOrderId()));
        handleDelivery(event);
    }

    private void handleTodo(final OrderEvent event) {
        logger.info("Handling TODO event for order: {}", event.getOrderId());
        kitchen.handle(event);
    }

    private void handleDelivered(OrderEvent event) {
        logger.info("Handling DELIVERED event for order: {}", event.getOrderId());
        commandBus.dispatch(new OrderDeliveredCommand(event.getOrderId()));
    }

    private void handleCook(OrderEvent event) {
        logger.info("Adding employee {} to order: {}", event.getEmployeeId(), event.getOrderId());
        commandBus.dispatch(new AddEmployeeToOrderCommand(event.getOrderId(), event.getEmployeeId()));
    }

    private void handleDelivery(final OrderEvent event) {
        logger.info("Adding employee {} to delivery: {}", event.getEmployeeId(), event.getOrderId());
        commandBus.dispatch(new AddEmployeeToDeliveryCommand(event.getOrderId(), event.getEmployeeId()));
    }

    private void handleReady(OrderEvent event) {
        logger.info("Handling READY event for order: {}", event.getOrderId());
        commandBus.dispatch(new OrderPreparedCommand(event.getOrderId()));
        handleCook(event);
    }

    private void handleToDeliver(OrderEvent event) {
        logger.info("Handling TO_DELIVER event for order: {}", event.getOrderId());
        deliveryHandler.handle(event);
    }
}
