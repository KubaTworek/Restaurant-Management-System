package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparingInt;

class Kitchen {
    private final Queue<Order> ordersQueue = new PriorityQueue<>(comparingInt(this::orderPriority));
    private final Queue<EmployeeId> cooksQueue = new LinkedList<>();
    private final DomainEventPublisher publisher;

    Kitchen(final DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    void handle(final Order order) {
        ordersQueue.add(order);
        processCooking();
    }

    void handle(final EmployeeEvent event) {
        cooksQueue.add(new EmployeeId(event.getEmployeeId()));
        processCooking();
    }

    void handle(final EmployeeId employeeId) {
        cooksQueue.add(employeeId);
        processCooking();
    }

    private void processCooking() {
        while (isExistsCookAndOrder()) {
            startCooking();
        }
    }

    private void startCooking() {
        final var cook = cooksQueue.poll();
        final var order = ordersQueue.poll();
        final int timeToCook = order.calculateCookingTime();
        startPreparingOrder(cook, order, timeToCook);
    }

    private void startPreparingOrder(EmployeeId cook, Order order, int time) {
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

    private boolean isExistsCookAndOrder() {
        return !ordersQueue.isEmpty() && !cooksQueue.isEmpty();
    }

    private int orderPriority(Order order) {
        TypeOfOrder orderType = order.getSnapshot().getTypeOfOrder();
        if (orderType == TypeOfOrder.ON_SITE || orderType == TypeOfOrder.TAKE_AWAY) {
            return -1;
        } else if (orderType == TypeOfOrder.DELIVERY) {
            return 1;
        } else {
            return 0;
        }
    }
}
