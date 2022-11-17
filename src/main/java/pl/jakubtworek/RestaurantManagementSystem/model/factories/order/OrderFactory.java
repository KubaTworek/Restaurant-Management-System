package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;

@Component
@RequiredArgsConstructor
public class OrderFactory {
    private final TypeOfOrderRepository typeOfOrderRepository;

    public OrderFormula createOrder(OrderRequest orderDTO){
        String typeOfOrderName = orderDTO.getTypeOfOrder();
        TypeOfOrder typeOfOrder = typeOfOrderRepository.findByType(typeOfOrderName).get();
        switch(typeOfOrderName){
            case "On-site":
                return new OnsiteFormula(orderDTO, typeOfOrder);
            case "Delivery":
                return new DeliveryFormula(orderDTO, typeOfOrder);
            default:
                return null;
        }
    }
}
