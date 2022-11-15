package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface OrderFormula {
    OrderDTO createOrder();
    default double countingOrderPrice(OrderRequest orderDTO){
        double price = 0;
        for(MenuItemRequest tempMenuItem : orderDTO.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }
    default String getTodayDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(localDateTime);
    }
    default String getTodayTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }
}
