package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.delivery.dto.EmployeeDelivery;
import pl.jakubtworek.order.delivery.dto.OrderDelivery;
import pl.jakubtworek.order.vo.OrderEvent;

abstract class AbstractDelivery {
    final DeliveryQueues queues;
    final DomainEventPublisher publisher;
    final Long timeToDelivery;

    AbstractDelivery(final DomainEventPublisher publisher, final Long timeToDelivery) {
        this.queues = new DeliveryQueues();
        this.publisher = publisher;
        this.timeToDelivery = timeToDelivery;
    }

    abstract void handle(final EmployeeEvent event);
    abstract void handle(final OrderEvent event);

    void processDeliveries() {
        while (queues.isExistsEmployeeAndOrder()) {
            startDelivering();
        }
    }

    private void startDelivering() {
        final var employee = queues.getFirstEmployee();
        final var order = queues.getFirstOrder();
        publisher.publish(new OrderEvent(
                order.orderId(),
                employee.employeeId(),
                order.orderType(),
                order.amountOfMenuItems(),
                OrderEvent.State.START_DELIVERY
        ));
        delivering(employee, order, timeToDelivery);
    }

    private void delivering(EmployeeDelivery employee, OrderDelivery order, Long time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(new OrderEvent(
                        order.orderId(),
                        employee.employeeId(),
                        order.orderType(),
                        order.amountOfMenuItems(),
                        OrderEvent.State.DELIVERED
                ));
                publisher.publish(new EmployeeEvent(
                        employee.employeeId(),
                        order.orderId(),
                        employee.job()
                ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
