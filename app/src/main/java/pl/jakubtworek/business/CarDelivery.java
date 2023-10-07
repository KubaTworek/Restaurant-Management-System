package pl.jakubtworek.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.business.queues.DeliveryQueue;
import pl.jakubtworek.business.queues.Observer;
import pl.jakubtworek.business.queues.OrdersMadeDeliveryQueue;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployeeQueryDto;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrderQueryDto;

import java.time.ZonedDateTime;

@Service
class CarDelivery extends Delivery implements Observer {

    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    CarDelivery(final OrderFacade orderFacade, final EmployeeFacade employeeFacade, final DeliveryQueue deliveryQueue,
                final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        super(orderFacade, employeeFacade);
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
        SimpleEmployeeQueryDto employee = deliveryQueue.get();
        SimpleOrderQueryDto order = ordersMadeDeliveryQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        employeeFacade.addOrderToEmployee(employee, order);
        startDeliveringOrder(employee, order, 5);
    }

    @Override
    boolean isExistsEmployeeAndOrder() {
        return ordersMadeDeliveryQueue.size() > 0 && deliveryQueue.size() > 0;
    }

    @Override
    void delivering(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            order.setHourAway(ZonedDateTime.now());
            orderFacade.update(order);
            deliveryQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
