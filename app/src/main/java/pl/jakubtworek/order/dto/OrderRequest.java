package pl.jakubtworek.order.dto;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderRequest {
    @NotNull(message = "Type of order cannot be null.")
    private String typeOfOrder;
    @NotNull(message = "Menu items cannot be null.")
    private List<String> menuItems;

    OrderRequest() {
    }

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