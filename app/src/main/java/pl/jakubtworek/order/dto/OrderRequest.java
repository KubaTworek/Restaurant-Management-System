package pl.jakubtworek.order.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    @NotNull(message = "Type of order cannot be null.")
    private final String typeOfOrder;
    @NotNull(message = "Menu items cannot be null.")
    private final List<String> menuItems;

    public OrderRequest(final String typeOfOrder, final List<String> menuItems) {
        this.typeOfOrder = typeOfOrder;
        this.menuItems = menuItems;
    }

    public String getTypeOfOrder() {
        return typeOfOrder;
    }

    public List<String> getMenuItems() {
        return menuItems;
    }
}