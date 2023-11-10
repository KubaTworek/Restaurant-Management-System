package pl.jakubtworek.order;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.order.vo.OrderEvent;

@Service
class OrderEventListener {
    private final OrderFacade orderFacade;

    OrderEventListener(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        if (event.getState() == OrderEvent.State.DELIVERED) {
            handleDeliveredEvent(event);
        }
        if (event.getEmployeeId() != null) {
            handleEmployeeEvent(event);
        }
    }

    private void handleDeliveredEvent(OrderEvent event) {
        orderFacade.setAsDelivered(event.getOrderId());
    }

    private void handleEmployeeEvent(OrderEvent event) {
        orderFacade.addEmployeeToOrder(event.getOrderId(), event.getEmployeeId());
    }
}
