package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.order.delivery.dto.EmployeeDelivery;
import pl.jakubtworek.order.delivery.dto.OrderDelivery;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.vo.OrderEvent;

class Kitchen {
    private final KitchenQueues queues;
    private final DomainEventPublisher publisher;

    Kitchen(final DomainEventPublisher publisher) {
        this.queues = new KitchenQueues();
        this.publisher = publisher;
    }

    void handle(final OrderEvent event) {
        queues.add(new OrderDelivery(
                event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems())
        );
        processCooking();
    }

    void handle(final EmployeeEvent event) {
        queues.add(new EmployeeDelivery(
                event.getEmployeeId(), event.getJob())
        );
        processCooking();
    }

    private void processCooking() {
        while (queues.isExistsCookAndOrder()) {
            startCooking();
        }
    }

    private void startCooking() {
        final var cook = queues.getFirstCook();
        final var order = queues.getFirstOrder();
        final int timeToCook = order.getAmountOfMenuItems();
        startPreparingOrder(cook, order, timeToCook);
    }

    private void startPreparingOrder(EmployeeDelivery cook, OrderDelivery order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(new OrderEvent(
                        order.getOrderId(),
                        cook.getEmployeeId(),
                        order.getOrderType(),
                        order.getAmountOfMenuItems(),
                        OrderEvent.State.READY
                ));
                publisher.publish(new EmployeeEvent(
                        cook.getEmployeeId(),
                        order.getOrderId(),
                        Job.COOK
                ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
