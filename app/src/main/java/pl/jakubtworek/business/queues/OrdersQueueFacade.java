package pl.jakubtworek.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.order.dto.SimpleOrderQueryDto;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.util.Objects;

@Component
public class OrdersQueueFacade {
    private final OrdersQueue ordersQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    OrdersQueueFacade(final OrdersQueue ordersQueue, final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        this.ordersQueue = ordersQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
    }

    public void addToQueue(SimpleOrderQueryDto order) {
        ordersQueue.add(order);
    }

    public void addMadeOrderToQueue(SimpleOrderQueryDto order) {
        if (Objects.equals(order.getTypeOfOrder(), TypeOfOrder.ON_SITE) || Objects.equals(order.getTypeOfOrder(), TypeOfOrder.TAKE_AWAY))
            ordersMadeOnsiteQueue.add(order);
        if (Objects.equals(order.getTypeOfOrder(), TypeOfOrder.DELIVERY)) ordersMadeDeliveryQueue.add(order);
    }
}

