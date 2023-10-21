package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

abstract class Delivery {
    final OrderFacade orderFacade;
    final EmployeeQueue employeeQueue;

    Delivery(OrderFacade orderFacade, EmployeeQueue employeeQueue) {
        this.orderFacade = orderFacade;
        this.employeeQueue = employeeQueue;
    }

    abstract void startDelivering();
    abstract boolean isExistsEmployeeAndOrder();

    void delivering(SimpleEmployee employee, SimpleOrder order, int time) {
        try {
            Thread.sleep(time);
            orderFacade.setAsDelivered(order);
            employeeQueue.add(employee);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}