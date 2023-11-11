package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparingInt;

class KitchenQueues {
    private final Queue<Order> orderQueue;
    private final Queue<Cook> cookQueue;

    KitchenQueues() {
        this.orderQueue = new PriorityQueue<>(comparingInt(this::orderPriority));
        this.cookQueue = new LinkedList<>();
    }

    void add(final Order order) {
        orderQueue.add(order);
    }

    void add(final Cook cook) {
        cookQueue.add(cook);
    }

    Order getFirstOrder() {
        return orderQueue.poll();
    }

    Cook getFirstCook() {
        return cookQueue.poll();
    }

    boolean isExistsCookAndOrder() {
        return !orderQueue.isEmpty() && !cookQueue.isEmpty();
    }

    private int orderPriority(Order order) {
        final var orderType = order.getSnapshot().getTypeOfOrder();
        if (orderType == TypeOfOrder.ON_SITE || orderType == TypeOfOrder.TAKE_AWAY) {
            return -1;
        } else if (orderType == TypeOfOrder.DELIVERY) {
            return 1;
        } else {
            return 0;
        }
    }
}
