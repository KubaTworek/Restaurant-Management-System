package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.EmployeeDelivery;
import pl.jakubtworek.order.dto.OrderDelivery;

import java.util.LinkedList;
import java.util.Queue;

class DeliveryQueues {
    private final Queue<OrderDelivery> orderQueue;
    private final Queue<EmployeeDelivery> employeeQueue;

    DeliveryQueues() {
        this.orderQueue = new LinkedList<>();
        this.employeeQueue = new LinkedList<>();
    }

    void add(final OrderDelivery order) {
        orderQueue.add(order);
    }

    void add(final EmployeeDelivery cook) {
        employeeQueue.add(cook);
    }

    OrderDelivery getFirstOrder() {
        return orderQueue.poll();
    }

    EmployeeDelivery getFirstEmployee() {
        return employeeQueue.poll();
    }

    boolean isExistsEmployeeAndOrder() {
        return !orderQueue.isEmpty() && !employeeQueue.isEmpty();
    }
}
