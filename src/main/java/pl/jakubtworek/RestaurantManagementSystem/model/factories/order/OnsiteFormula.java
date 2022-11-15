package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import lombok.RequiredArgsConstructor;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

@RequiredArgsConstructor
public class OnsiteFormula implements OrderFormula{
    private final OrderRequest orderRequest;
    private final TypeOfOrder typeOfOrder;
    private final OrdersQueue ordersQueue;

    @Override
    public Order createOrder() {
        Order order = Order.builder()
                .id(0L)
                .typeOfOrder(typeOfOrder)
                .hourOrder(getTodayTime())
                .hourAway(null)
                .date(getTodayDate())
                .price(countingOrderPrice(orderRequest))
                .menuItems(orderRequest.convertRequestToEntity().getMenuItems())
                .employees(null)
                .build();
        ordersQueue.add(order);
        return order;
    }
}