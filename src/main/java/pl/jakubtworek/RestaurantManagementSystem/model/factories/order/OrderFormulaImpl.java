package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;

@RequiredArgsConstructor
public class OrderFormulaImpl implements OrderFormula{
    private final OrderRequest orderRequest;
    private final TypeOfOrder typeOfOrder;

    @Override
    public OrderDTO createOrder(
    ) {
        String time = getNowTime();
        String date = getTodayDate();
        double price = calculateOrderPrice(orderRequest.getMenuItems());
        List<MenuItem> menuItems = orderRequest.convertRequestToEntity().getMenuItems();

        return OrderDTO.builder()
                .id(0L)
                .typeOfOrder(typeOfOrder)
                .hourOrder(time)
                .hourAway(null)
                .date(date)
                .price(price)
                .menuItems(menuItems)
                .employees(null)
                .build();
    }

}
