package pl.jakubtworek.restaurant.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.business.queues.DeliveryQueue;
import pl.jakubtworek.restaurant.business.queues.Observer;
import pl.jakubtworek.restaurant.business.queues.OrdersMadeDeliveryQueue;
import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.order.OrderDto;
import pl.jakubtworek.restaurant.order.OrderService;

import java.time.ZonedDateTime;

@Service
class CarDelivery extends Delivery implements Observer {

    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    CarDelivery(OrderService orderService, DeliveryQueue deliveryQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        super(orderService);
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
        EmployeeDto employee = deliveryQueue.get();
        OrderDto order = ordersMadeDeliveryQueue.get();
        order.add(employee);
        startDeliveringOrder(employee, order, 5);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeDeliveryQueue.size() > 0 && deliveryQueue.size() > 0;
    }

    @Override
    void delivering(EmployeeDto employee, OrderDto order, int time) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            order.setHourAway(ZonedDateTime.now());
            orderService.update(order);
            deliveryQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
