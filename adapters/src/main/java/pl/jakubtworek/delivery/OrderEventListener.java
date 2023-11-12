package pl.jakubtworek.delivery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

@Service
class OrderEventListener {
    private final OrderFacade orderFacade;
    private final Kitchen kitchen;
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;

    OrderEventListener(final OrderFacade orderFacade, final Kitchen kitchen, final CarDelivery carDelivery, final WaiterDelivery waiterDelivery) {
        this.orderFacade = orderFacade;
        this.kitchen = kitchen;
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        switch (event.getState()) {
            case TODO:
                handleTodo(event);
                break;
            case DELIVERED:
                handleDelivered(event);
                break;
            case READY:
                handleReady(event.getOrderType(), event);
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
        orderFacade.setAsDelivered(event.getOrderId());
    }

    private void handleEmployee(OrderEvent event) {
        orderFacade.addEmployeeToOrder(event.getOrderId(), event.getEmployeeId());
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
