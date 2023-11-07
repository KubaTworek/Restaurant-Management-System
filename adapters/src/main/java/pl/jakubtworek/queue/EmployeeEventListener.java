package pl.jakubtworek.queue;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.OrderFacade;

@Service
class EmployeeEventListener {
    private final Kitchen kitchen;
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;
    private final OrderFacade orderFacade;

    EmployeeEventListener(final Kitchen kitchen, final CarDelivery carDelivery, final WaiterDelivery waiterDelivery, final OrderFacade orderFacade) {
        this.kitchen = kitchen;
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
        this.orderFacade = orderFacade;
    }

    @EventListener
    public void on(EmployeeEvent event) {
        if (event.getOrderId() != null) {
            orderFacade.addEmployeeToOrder(event.getOrderId(), event.getEmployeeId());
        }
        switch (event.getJob()) {
            case WAITER:
                waiterDelivery.handle(event);
            break;
            case DELIVERY:
                carDelivery.handle(event);
            break;
            case COOK:
                kitchen.handle(event);
            break;
        }
    }
}
