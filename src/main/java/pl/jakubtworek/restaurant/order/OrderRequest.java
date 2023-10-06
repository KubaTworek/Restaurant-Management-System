package pl.jakubtworek.restaurant.order;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
class OrderRequest {
    @NotNull(message = "Type of order cannot be null.")
    private String typeOfOrder;
    @NotNull(message = "Menu items cannot be null.")
    private List<String> menuItems;
}