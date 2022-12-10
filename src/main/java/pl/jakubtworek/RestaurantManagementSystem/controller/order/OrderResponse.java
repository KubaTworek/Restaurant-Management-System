package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {
    private UUID id;
    private double price;
    private String date;
    private String hourOrder;
    private String hourAway;
    private TypeOfOrderResponse typeOfOrder;
    private List<MenuItemResponse> menuItems;
    private List<EmployeeResponse> employees;
}