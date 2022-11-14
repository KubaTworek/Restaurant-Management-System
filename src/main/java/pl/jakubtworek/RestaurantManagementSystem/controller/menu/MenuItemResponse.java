package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {

    private Long id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    private double price;
}