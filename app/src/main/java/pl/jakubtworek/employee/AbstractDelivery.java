package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.OrderId;

import java.util.LinkedList;
import java.util.Queue;

abstract class AbstractDelivery {
    private final Queue<OrderId> ordersQueue = new LinkedList<>();
    private final Queue<Employee> employeeQueue = new LinkedList<>();
    private final DomainEventPublisher publisher;

    AbstractDelivery(final DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    void handle(final Employee employee) {
        employeeQueue.add(employee);
        processDeliveries();
    }

    void handle(final OrderEvent event) {
        ordersQueue.add(new OrderId(event.getOrderId()));
        processDeliveries();
    }

    private void processDeliveries() {
        while (isExistsEmployeeAndOrder()) {
            startDelivering();
        }
    }

    private void startDelivering() {
        final var employee = employeeQueue.poll();
        final var order = ordersQueue.poll();
        delivering(employee, order, 0); // fixme: specific time
    }

    private void delivering(Employee employee, OrderId order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(employee.deliveredOrderWithId(order));
                handle(employee);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private boolean isExistsEmployeeAndOrder() {
        return !ordersQueue.isEmpty() && !employeeQueue.isEmpty();
    }
}