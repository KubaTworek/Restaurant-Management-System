package pl.jakubtworek.RestaurantManagementSystem.model.business.queues;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

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
        if(orderDTO.getTypeOfOrder().getId()==1) ordersMadeOnsiteQueue.add(orderDTO);
        if(orderDTO.getTypeOfOrder().getId()==2) ordersMadeDeliveryQueue.add(orderDTO);
    }
}
