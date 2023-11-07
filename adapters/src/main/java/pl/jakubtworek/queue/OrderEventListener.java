package pl.jakubtworek.queue;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.vo.OrderEvent;

@Service
class OrderEventListener {
    private final Kitchen kitchen;
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;
    private final OrderFacade orderFacade;

    OrderEventListener(final Kitchen kitchen, final CarDelivery carDelivery,
                       final WaiterDelivery waiterDelivery, final OrderFacade orderFacade) {
        this.kitchen = kitchen;
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
        this.orderFacade = orderFacade;
    }

    @EventListener
    public void on(OrderEvent event) {
        switch (event.getState()) {
            case TODO:
                kitchen.handle(event);
                break;
            case READY: {
                switch (event.getOrderType()) {
                    case ON_SITE:
                    case TAKE_AWAY:
                        waiterDelivery.handle(event);
                        break;
                    case DELIVERY:
                        carDelivery.handle(event);
                        break;
                }
            }
            case DELIVERED:
                orderFacade.setAsDelivered(event.getOrderId());
                break;
        }
    }
}
