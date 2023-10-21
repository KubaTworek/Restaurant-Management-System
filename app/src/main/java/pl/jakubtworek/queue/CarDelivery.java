package pl.jakubtworek.queue;

import pl.jakubtworek.order.OrderFacade;

class CarDelivery extends Delivery implements Observer {

    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    CarDelivery(final OrderFacade orderFacade, final DeliveryQueue deliveryQueue, final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        super(orderFacade, deliveryQueue);
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
    public void startDelivering() {
        final var employee = employeeQueue.get();
        final var order = ordersMadeDeliveryQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        delivering(employee, order, 0);
    }

    @Override
    public boolean isExistsEmployeeAndOrder() {
        return ordersMadeDeliveryQueue.size() > 0 && employeeQueue.size() > 0;
    }
}
