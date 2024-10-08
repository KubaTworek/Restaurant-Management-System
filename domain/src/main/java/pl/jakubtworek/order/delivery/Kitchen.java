package pl.jakubtworek.order.delivery;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.order.delivery.dto.EmployeeDelivery;
import pl.jakubtworek.order.delivery.dto.OrderDelivery;
import pl.jakubtworek.order.vo.OrderEvent;

class Kitchen {
    private final KitchenQueues queues;
    private final DomainEventPublisher publisher;
    private final Long multiplierTimeToCook;

    Kitchen(final DomainEventPublisher publisher, final Long multiplierTimeToCook) {
        this.queues = new KitchenQueues();
        this.publisher = publisher;
        this.multiplierTimeToCook = multiplierTimeToCook;
    }

    void handle(final OrderEvent event) {
        queues.add(new OrderDelivery(
                event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems(), event.getDistrict())
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
        final int timeToCook = order.amountOfMenuItems();
        startPreparingOrder(cook, order, timeToCook);
    }

    private void startPreparingOrder(EmployeeDelivery cook, OrderDelivery order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time * multiplierTimeToCook);
                publisher.publish(new OrderEvent(
                        order.orderId(),
                        cook.employeeId(),
                        order.orderType(),
                        order.amountOfMenuItems(),
                        order.district(),
                        OrderEvent.State.READY
                ));
                publisher.publish(new EmployeeEvent(
                        cook.employeeId(),
                        order.orderId(),
                        Job.COOK
                ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
