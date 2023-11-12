package pl.jakubtworek.order.delivery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.common.CommandBus;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

@Service
class OrderEventListener {
    private final Kitchen kitchen;
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;
    private final CommandBus commandBus;

    OrderEventListener(final Kitchen kitchen,
                       final CarDelivery carDelivery,
                       final WaiterDelivery waiterDelivery,
                       final CommandBus commandBus
    ) {
        this.kitchen = kitchen;
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
        this.commandBus = commandBus;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        switch (event.getState()) {
            case TODO:
                handleTodo(event);
                break;
            case READY:
                handleReady(event.getOrderType(), event);
                break;
            case DELIVERED:
                handleDelivered(event);
                break;
        }

        if (event.getEmployeeId() != null) {
            handleEmployee(event);
        }
    }

    private void handleTodo(final OrderEvent event) {
        kitchen.handle(event);
    }

    private void handleDelivered(OrderEvent event) {
        commandBus.dispatch(new DeliverOrderCommand(event.getOrderId()));
    }

    private void handleEmployee(OrderEvent event) {
        commandBus.dispatch(new AddEmployeeToOrderCommand(event.getOrderId(), event.getEmployeeId()));
    }

    private void handleReady(TypeOfOrder orderType, OrderEvent event) {
        switch (orderType) {
            case ON_SITE:
            case TAKE_AWAY:
                waiterDelivery.handle(event);
                break;
            case DELIVERY:
                carDelivery.handle(event);
                break;
        }
    }
}
