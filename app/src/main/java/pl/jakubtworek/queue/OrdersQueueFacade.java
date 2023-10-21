package pl.jakubtworek.queue;

import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

public class OrdersQueueFacade {
    private final OrdersQueue ordersQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    OrdersQueueFacade(final OrdersQueue ordersQueue, final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        this.ordersQueue = ordersQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
    }

    public void addToQueue(SimpleOrder order) {
        ordersQueue.add(order);
    }

    public void addReadyToQueue(SimpleOrder order) {
        final var orderType = order.getTypeOfOrder();

        if (orderType == TypeOfOrder.ON_SITE || orderType == TypeOfOrder.TAKE_AWAY) {
            ordersMadeOnsiteQueue.add(order);
        } else if (orderType == TypeOfOrder.DELIVERY) {
            ordersMadeDeliveryQueue.add(order);
        }
    }
}

