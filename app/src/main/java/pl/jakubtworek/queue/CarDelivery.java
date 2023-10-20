package pl.jakubtworek.queue;

import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

@Service
class CarDelivery extends Delivery implements Observer {

    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    CarDelivery(final OrderFacade orderFacade, final DeliveryQueue deliveryQueue,
                final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        super(orderFacade);
        this.deliveryQueue = deliveryQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        deliveryQueue.registerObserver(this);
        ordersMadeDeliveryQueue.registerObserver(this);
    }

    @Override
    public void update() {
        if (isExistsEmployeeAndOrder()) {
            startDelivering();
        }
    }

    @Override
    void startDelivering() {
        SimpleEmployee employee = deliveryQueue.get();
        SimpleOrder order = ordersMadeDeliveryQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        startDeliveringOrder(employee, order, 0, deliveryQueue);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeDeliveryQueue.size() > 0 && deliveryQueue.size() > 0;
    }
}
