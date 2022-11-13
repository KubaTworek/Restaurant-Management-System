package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderRequest {

    private Long id;

    @NotNull(message = "Type of order cannot be null.")
    private String typeOfOrder;

    @NotNull(message = "Menu items cannot be null.")
    private List<MenuItemRequest> menuItems;

    public Order convertRequestToEntity() {
        return new ModelMapper().map(this, Order.class);
    }
}