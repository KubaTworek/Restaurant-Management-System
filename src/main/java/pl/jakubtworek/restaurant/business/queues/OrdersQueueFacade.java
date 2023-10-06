package pl.jakubtworek.restaurant.business.queues;

import org.springframework.stereotype.Component;
import pl.jakubtworek.restaurant.order.OrderDto;
import pl.jakubtworek.restaurant.order.TypeOfOrder;

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

    public void addToQueue(OrderDto orderDTO) {
        ordersQueue.add(orderDTO);
    }

    public void addMadeOrderToQueue(OrderDto orderDTO) {
        if (Objects.equals(orderDTO.getTypeOfOrder(), TypeOfOrder.ON_SITE) || Objects.equals(orderDTO.getTypeOfOrder(), TypeOfOrder.TAKE_AWAY))
            ordersMadeOnsiteQueue.add(orderDTO);
        if (Objects.equals(orderDTO.getTypeOfOrder(), TypeOfOrder.DELIVERY)) ordersMadeDeliveryQueue.add(orderDTO);
    }
}

