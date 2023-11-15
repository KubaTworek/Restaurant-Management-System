package pl.jakubtworek.order.delivery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.common.CommandBus;
import pl.jakubtworek.order.command.AddEmployeeToOrderCommand;
import pl.jakubtworek.order.command.DeliverOrderCommand;
import pl.jakubtworek.order.vo.TypeOfOrder;
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
            case TODO -> handleTodo(event);
            case READY -> handleReady(event.getOrderType(), event);
            case DELIVERED -> handleDelivered(event);
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
            case ON_SITE, TAKE_AWAY -> waiterDelivery.handle(event);
            case DELIVERY -> carDelivery.handle(event);
        }
    }
}
