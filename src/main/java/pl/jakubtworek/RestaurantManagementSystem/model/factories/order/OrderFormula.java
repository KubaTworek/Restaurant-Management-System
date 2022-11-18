package pl.jakubtworek.RestaurantManagementSystem.model.factories.order;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

public interface OrderFormula {
    OrderDTO createOrder();

    default double calculateOrderPrice(List<MenuItemRequest> menuItems){
        return menuItems.stream()
                .mapToDouble(MenuItemRequest::getPrice)
                .sum();
    }

    default String getTodayDate(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(localDateTime);
    }

    default String getNowTime(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }
}
