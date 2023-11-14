package pl.jakubtworek.order.delivery;

import pl.jakubtworek.order.delivery.dto.EmployeeDelivery;
import pl.jakubtworek.order.delivery.dto.OrderDelivery;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

import static java.util.Comparator.comparingInt;

class KitchenQueues {
    private final Queue<OrderDelivery> orderQueue;
    private final Queue<EmployeeDelivery> cookQueue;

    KitchenQueues() {
        this.orderQueue = new PriorityQueue<>(comparingInt(this::orderPriority));
        this.cookQueue = new LinkedList<>();
    }

    void add(final OrderDelivery order) {
        orderQueue.add(order);
    }

    void add(final EmployeeDelivery cook) {
        cookQueue.add(cook);
    }

    OrderDelivery getFirstOrder() {
        return orderQueue.poll();
    }

    EmployeeDelivery getFirstCook() {
        return cookQueue.poll();
    }

    boolean isExistsCookAndOrder() {
        return !orderQueue.isEmpty() && !cookQueue.isEmpty();
    }

    private int orderPriority(OrderDelivery order) {
        final var orderType = order.orderType();
        if (orderType == TypeOfOrder.ON_SITE || orderType == TypeOfOrder.TAKE_AWAY) {
            return -1;
        } else if (orderType == TypeOfOrder.DELIVERY) {
            return 1;
        } else {
            return 0;
        }
    }
}
