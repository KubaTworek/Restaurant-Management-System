package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

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
    @NotNull(message = "User cannot be null.")
    private String username = SecurityContextHolder.getContext().getAuthentication().getName();

    public OrderDTO convertRequestToDTO() {
        return new ModelMapper().map(this, OrderDTO.class);
    }
}