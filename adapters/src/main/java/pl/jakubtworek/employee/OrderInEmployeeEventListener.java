package pl.jakubtworek.employee;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import static pl.jakubtworek.order.vo.OrderEvent.State.READY;

@Service
class OrderInEmployeeEventListener {
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;

    OrderInEmployeeEventListener(CarDelivery carDelivery, WaiterDelivery waiterDelivery) {
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
    }

    @EventListener
    public void handleOrderEvent(OrderEvent event) {
        if (READY.equals(event.getState())) {
            handleOrderType(event.getOrderType(), event);
        }
    }

    private void handleOrderType(TypeOfOrder orderType, OrderEvent event) {
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
