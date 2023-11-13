package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.dto.EmployeeDelivery;
import pl.jakubtworek.order.dto.OrderDelivery;
import pl.jakubtworek.order.vo.OrderEvent;

abstract class AbstractDelivery {
    private final DeliveryQueues queues;
    private final DomainEventPublisher publisher;

    AbstractDelivery(final DomainEventPublisher publisher) {
        this.queues = new DeliveryQueues();
        this.publisher = publisher;
    }

    void handle(final EmployeeEvent event) {
        queues.add(new EmployeeDelivery(event.getEmployeeId(), event.getJob()));
        processDeliveries();
    }

    void handle(final OrderEvent event) {
        queues.add(new OrderDelivery(event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems()));
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

    private void delivering(EmployeeDelivery employee, OrderDelivery order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(new OrderEvent(
                        order.getOrderId(),
                        employee.getEmployeeId(),
                        order.getOrderType(),
                        order.getAmountOfMenuItems(),
                        OrderEvent.State.DELIVERED
                ));
                publisher.publish(new EmployeeEvent(
                        employee.getEmployeeId(),
                        order.getOrderId(),
                        employee.getJob()
                ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
