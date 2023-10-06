package pl.jakubtworek.restaurant.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.business.queues.Observer;
import pl.jakubtworek.restaurant.business.queues.OrdersMadeOnsiteQueue;
import pl.jakubtworek.restaurant.business.queues.WaiterQueue;
import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.order.OrderDto;
import pl.jakubtworek.restaurant.order.OrderService;

import java.time.ZonedDateTime;

@Service
class WaiterDelivery extends Delivery implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    WaiterDelivery(OrderService orderService, WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue) {
        super(orderService);
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
        EmployeeDto employee = waiterQueue.get();
        OrderDto order = ordersMadeOnsiteQueue.get();
        order.add(employee);
        startDeliveringOrder(employee, order, 2);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeOnsiteQueue.size() > 0 && waiterQueue.size() > 0;
    }

    @Override
    void delivering(EmployeeDto employee, OrderDto order, int time) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            order.setHourAway(ZonedDateTime.now());
            orderService.update(order);
            waiterQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
