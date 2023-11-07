package pl.jakubtworek.queue;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.employee.vo.EmployeeEvent;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Queue;

class Kitchen {
    private final Queue<OrderDto> ordersQueue = new PriorityQueue<>(new OrderComparator());
    private final Queue<EmployeeDto> cooksQueue = new LinkedList<>();
    private final DomainEventPublisher publisher;

    Kitchen(final DomainEventPublisher publisher) {
        this.publisher = publisher;
    }

    void handle(final OrderEvent event) {
        ordersQueue.add(new OrderDto(event.getOrderId(), event.getOrderType(), event.getAmountOfMenuItems()));
        processCooking();
    }

    void handle(final EmployeeEvent event) {
        cooksQueue.add(new EmployeeDto(event.getEmployeeId(), event.getJob()));
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
        final int timeToCook = calculateCookingTime(order.getAmountOfMenuItems());
        startPreparingOrder(cook, order, timeToCook);
    }

    private int calculateCookingTime(int numberOfMenuItems) {
        return numberOfMenuItems * 1;
    } // 10 000

    private void startPreparingOrder(EmployeeDto cook, OrderDto order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                publisher.publish(new OrderEvent(
                        order.getOrderId(),
                        order.getOrderType(),
                        order.getAmountOfMenuItems(),
                        OrderEvent.State.READY
                ));
                publisher.publish(new EmployeeEvent(
                        cook.getEmployeeId(),
                        order.getOrderId(),
                        cook.getJob()
                ));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private boolean isExistsCookAndOrder() {
        return !ordersQueue.isEmpty() && !cooksQueue.isEmpty();
    }

    private static class OrderComparator implements Comparator<OrderDto> {
        @Override
        public int compare(OrderDto o1, OrderDto o2) {
            return Integer.compare(isOrderOnsite(o1), isOrderOnsite(o2));
        }

        private int isOrderOnsite(OrderDto o1) {
            if (Objects.equals(o1.getOrderType(), TypeOfOrder.ON_SITE) || Objects.equals(o1.getOrderType(), TypeOfOrder.TAKE_AWAY))
                return -1;
            if (Objects.equals(o1.getOrderType(), TypeOfOrder.DELIVERY))
                return 1;
            return 0;
        }
    }
}
