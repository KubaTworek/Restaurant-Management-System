package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderRequest {
    @NotNull(message = "Type of order cannot be null.")
    private String typeOfOrder;
    @NotNull(message = "Menu items cannot be null.")
    private List<MenuItemRequest> menuItems;
}