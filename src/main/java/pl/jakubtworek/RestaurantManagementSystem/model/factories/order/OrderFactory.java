package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

@Component
@RequiredArgsConstructor
public class OrderFactory {
    public OrderFormula createOrder(
            OrderRequest orderDTO,
            TypeOfOrder typeOfOrder
    ){
        return new OrderFormulaImpl(orderDTO, typeOfOrder);
    }
}
