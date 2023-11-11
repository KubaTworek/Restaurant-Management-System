package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.order.vo.OrderEvent;

abstract class AbstractDelivery {
    private final DeliveryQueues queues;
    private final DomainEventPublisher publisher;

    AbstractDelivery(final DomainEventPublisher publisher) {
        this.queues = new DeliveryQueues();
        this.publisher = publisher;
    }

    void handle(final Employee employee) {
        queues.add(employee);
        processDeliveries();
    }

    void handle(final OrderEvent event) {
        queues.add(new Order(event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems()));
        processDeliveries();
    }

    private void processDeliveries() {
        while (queues.isExistsEmployeeAndOrder()) {
            startDelivering();
        }
    }

    private void startDelivering() {
        final var employee = queues.getFirstEmployee();
        final var order = queues.getFirstOrder();
        delivering(employee, order, 0); // fixme: specific time
    }

    private void delivering(Employee employee, Order order, int time) {
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
}