package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

@Service
public class WaiterDelivery extends Delivery implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    public WaiterDelivery(OrderService orderService, WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue) {
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
    protected void startDelivering() {
        EmployeeDTO employee = waiterQueue.get();
        OrderDTO order = ordersMadeOnsiteQueue.get();
        order.add(employee);
        startDeliveringOrder(employee, order, 2);
    }

    @Override
    protected boolean isExistsEmployeeAndOrder() {
        return ordersMadeOnsiteQueue.size() > 0 && waiterQueue.size() > 0;
    }

    @Override
    protected void delivering(EmployeeDTO employee, OrderDTO order, int time) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            order.setHourAway(setHourAwayToOrder());
            orderService.update(order);
            waiterQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
