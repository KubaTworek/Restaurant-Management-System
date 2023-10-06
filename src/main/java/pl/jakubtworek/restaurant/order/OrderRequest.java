package pl.jakubtworek.restaurant.order;

import javax.validation.constraints.NotNull;
import java.util.List;

class OrderRequest {
    @NotNull(message = "Type of order cannot be null.")
    private String typeOfOrder;
    @NotNull(message = "Menu items cannot be null.")
    private List<String> menuItems;

    String getTypeOfOrder() {
        return typeOfOrder;
    }

    List<String> getMenuItems() {
        return menuItems;
    }
}