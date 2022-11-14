package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemRequest {

    private Long id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    private double price;

    @NotNull(message = "Menu cannot be null.")
    private String menu;
}