package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;

import java.util.List;

@RequiredArgsConstructor
public class OrderFormulaImpl implements OrderFormula{
    private final OrderRequest orderRequest;
    private final TypeOfOrderDTO typeOfOrder;

    @Override
    public OrderDTO createOrder(
    ) {
        String time = getNowTime();
        String date = getTodayDate();
        double price = calculateOrderPrice(orderRequest.getMenuItems());
        List<MenuItemDTO> menuItems = orderRequest.convertRequestToDTO().getMenuItems();

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
