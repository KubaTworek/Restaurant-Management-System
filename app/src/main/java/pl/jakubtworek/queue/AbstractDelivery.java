package pl.jakubtworek.queue;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.vo.OrderEvent;

import java.util.LinkedList;
import java.util.Queue;

abstract class AbstractDelivery {
    private final Queue<OrderDto> ordersQueue = new LinkedList<>();
    private final Queue<EmployeeDto> employeeQueue = new LinkedList<>();
    private final DomainEventPublisher publisher;

    public AbstractDelivery(final DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    public void handle(final OrderEvent event) {
        ordersQueue.add(new OrderDto(event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems()));
        processDeliveries();
    }

    public void handle(final EmployeeEvent event) {
        employeeQueue.add(new EmployeeDto(event.getEmployeeId(), event.getJob()));
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

    private  void delivering(EmployeeDto employee, OrderDto order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(new OrderEvent(
                        order.getOrderId(),
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

    private boolean isExistsEmployeeAndOrder() {
        return !ordersQueue.isEmpty() && !employeeQueue.isEmpty();
    }
}