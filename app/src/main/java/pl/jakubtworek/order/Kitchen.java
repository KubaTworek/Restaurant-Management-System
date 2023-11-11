package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;

class Kitchen {
    private final KitchenQueues queues;
    private final DomainEventPublisher publisher;

    Kitchen(final DomainEventPublisher publisher) {
        this.queues = new KitchenQueues();
        this.publisher = publisher;
    }

    void handle(final Order order) {
        queues.add(order);
        processCooking();
    }

    void handle(final EmployeeEvent event) {
        queues.add(new Cook(event.getEmployeeId()));
        processCooking();
    }

    void handle(final Cook cook) {
        queues.add(cook);
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
        final int timeToCook = order.calculateCookingTime();
        startPreparingOrder(cook, order, timeToCook);
    }

    private void startPreparingOrder(Cook cook, Order order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(order.wasCookedBy(cook));
                handle(cook);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }
}
