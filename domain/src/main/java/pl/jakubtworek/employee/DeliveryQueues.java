package pl.jakubtworek.employee;

import java.util.LinkedList;
import java.util.Queue;

class DeliveryQueues {
    private final Queue<Order> orderQueue;
    private final Queue<Employee> employeeQueue;

    DeliveryQueues() {
        this.orderQueue = new LinkedList<>();
        this.employeeQueue = new LinkedList<>();
    }

    void add(final Order order) {
        orderQueue.add(order);
    }

    void add(final Employee cook) {
        employeeQueue.add(cook);
    }

    Order getFirstOrder() {
        return orderQueue.poll();
    }

    Employee getFirstEmployee() {
        return employeeQueue.poll();
    }

    boolean isExistsEmployeeAndOrder() {
        return !orderQueue.isEmpty() && !employeeQueue.isEmpty();
    }
}
