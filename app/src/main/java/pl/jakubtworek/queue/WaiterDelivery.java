package pl.jakubtworek.queue;

import pl.jakubtworek.order.OrderFacade;

class WaiterDelivery extends Delivery implements Observer {

    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    WaiterDelivery(final OrderFacade orderFacade, final WaiterQueue waiterQueue, final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue) {
        super(orderFacade, waiterQueue);
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        waiterQueue.registerObserver(this);
        ordersMadeOnsiteQueue.registerObserver(this);
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
        final var order = ordersMadeOnsiteQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        delivering(employee, order, 0);
    }

    @Override
    public boolean isExistsEmployeeAndOrder() {
        return ordersMadeOnsiteQueue.size() > 0 && employeeQueue.size() > 0;
    }
}
