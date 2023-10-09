package pl.jakubtworek.queue;

import org.springframework.stereotype.Service;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

@Service
class WaiterDelivery extends Delivery implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    WaiterDelivery(final OrderFacade orderFacade, final EmployeeFacade employeeFacade, final WaiterQueue waiterQueue,
                   final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue) {
        super(orderFacade, employeeFacade);
        this.waiterQueue = waiterQueue;
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
    void startDelivering() {
        SimpleEmployee employee = waiterQueue.get();
        SimpleOrder order = ordersMadeOnsiteQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        employeeFacade.addOrderToEmployee(employee, order);
        startDeliveringOrder(employee, order, 2, waiterQueue);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeOnsiteQueue.size() > 0 && waiterQueue.size() > 0;
    }
}
