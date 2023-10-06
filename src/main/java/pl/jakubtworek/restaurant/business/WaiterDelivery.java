package pl.jakubtworek.restaurant.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.business.queues.Observer;
import pl.jakubtworek.restaurant.business.queues.OrdersMadeOnsiteQueue;
import pl.jakubtworek.restaurant.business.queues.WaiterQueue;
import pl.jakubtworek.restaurant.employee.EmployeeFacade;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;
import pl.jakubtworek.restaurant.order.OrderFacade;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

import java.time.ZonedDateTime;

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
        SimpleEmployeeQueryDto employee = waiterQueue.get();
        SimpleOrderQueryDto order = ordersMadeOnsiteQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        employeeFacade.addOrderToEmployee(employee, order);
        startDeliveringOrder(employee, order, 2);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeOnsiteQueue.size() > 0 && waiterQueue.size() > 0;
    }

    @Override
    void delivering(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            order.setHourAway(ZonedDateTime.now());
            orderFacade.update(order);
            waiterQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
