package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;


@Component
@RequiredArgsConstructor
public class OrderFactory {
    public OrderFormula createOrder(
            OrderRequest orderDTO,
            TypeOfOrderDTO typeOfOrderDTO
    ){
        return new OrderFormulaImpl(orderDTO, typeOfOrderDTO);
    }
}
