package pl.jakubtworek.RestaurantManagementSystem.model.factories;

import org.springframework.stereotype.Component;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;


@Component
public class OrderFactory {

    public OrderDTO createOrder(
            OrderRequest orderRequest,
            TypeOfOrderDTO typeOfOrderDTO,
            List<MenuItemDTO> menuItemDTOList,
            UserDTO userDTO
    ) {
        String time = getNowTime();
        String date = getTodayDate();
        double price = calculateOrderPrice(orderRequest.getMenuItems());

        return OrderDTO.builder()
                .typeOfOrder(typeOfOrderDTO)
                .hourOrder(time)
                .hourAway(null)
                .date(date)
                .price(price)
                .menuItems(menuItemDTOList)
                .employees(null)
                .userDTO(userDTO)
                .build();
    }

    private double calculateOrderPrice(List<MenuItemRequest> menuItems) {
        return menuItems.stream()
                .mapToDouble(MenuItemRequest::getPrice)
                .sum();
    }

    private String getTodayDate() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        return date.format(localDateTime);
    }

    private String getNowTime() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }
}
