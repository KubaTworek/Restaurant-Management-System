package pl.jakubtworek.order.delivery;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.vo.EmployeeEvent;

@Service
class EmployeeEventListener {
    private final Kitchen kitchen;
    private final CarDelivery carDelivery;
    private final WaiterDelivery waiterDelivery;

    EmployeeEventListener(final Kitchen kitchen, final CarDelivery carDelivery, final WaiterDelivery waiterDelivery) {
        this.kitchen = kitchen;
        this.carDelivery = carDelivery;
        this.waiterDelivery = waiterDelivery;
    }

    @EventListener
    public void on(EmployeeEvent event) {
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
