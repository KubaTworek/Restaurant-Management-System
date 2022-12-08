package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;

import java.util.List;

@RequiredArgsConstructor
public class OrderFormulaImpl implements OrderFormula{
    private final OrderRequest orderRequest;
    private final TypeOfOrderDTO typeOfOrder;
    private final List<MenuItemDTO> menuItems;
    private final UserDTO userDTO;

    @Override
    public OrderDTO createOrder(
    ) {
        String time = getNowTime();
        String date = getTodayDate();
        double price = calculateOrderPrice(orderRequest.getMenuItems());

        return OrderDTO.builder()
                .id(0L)
                .typeOfOrder(typeOfOrder)
                .hourOrder(time)
                .hourAway(null)
                .date(date)
                .price(price)
                .menuItems(menuItems)
                .employees(null)
                .userDTO(userDTO)
                .build();
    }

}
