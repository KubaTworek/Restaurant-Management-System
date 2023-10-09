package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

abstract class Delivery {
    final OrderFacade orderFacade;

    Delivery(final OrderFacade orderFacade) {
        this.orderFacade = orderFacade;
    }

    abstract void startDelivering();

    abstract boolean isExistsEmployeeAndOrder();

    void delivering(SimpleEmployee employee, SimpleOrder order, int time, EmployeeQueue queue) {
        int timeToDelivery = time * 1000;
        try {
            Thread.sleep(timeToDelivery);
            orderFacade.setAsDelivered(order);
            queue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    ;

    void startDeliveringOrder(SimpleEmployee employee, SimpleOrder order, int time, EmployeeQueue queue) {
        Runnable r = () -> delivering(employee, order, time, queue);
        new Thread(r).start();
    }
}
