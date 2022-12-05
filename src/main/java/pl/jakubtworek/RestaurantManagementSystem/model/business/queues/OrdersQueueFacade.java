package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import java.util.Objects;

@RequiredArgsConstructor
@Component
public class OrdersQueueFacade {
    private final OrdersQueue ordersQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    public void addToQueue(OrderDTO orderDTO){
        ordersQueue.add(orderDTO);
    }

    public void addMadeOrderToQueue(OrderDTO orderDTO){
        if(Objects.equals(orderDTO.getTypeOfOrder().getType(), "On-site")) ordersMadeOnsiteQueue.add(orderDTO);
        if(Objects.equals(orderDTO.getTypeOfOrder().getType(), "Delivery")) ordersMadeDeliveryQueue.add(orderDTO);
    }
}

