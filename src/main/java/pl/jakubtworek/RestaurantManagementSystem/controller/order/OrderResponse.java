package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse extends RepresentationModel<OrderResponse> {

    private Long id;

    private double price;

    private String date;

    private String hourOrder;

    private String hourAway;

    @NotNull(message = "Type of order cannot be null.")
    private TypeOfOrderResponse typeOfOrder;

    @NotNull(message = "Menu items cannot be null.")
    private List<MenuItemResponse> menuItems;

    private List<EmployeeResponse> employees;
}