package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuItemRequest {

    private Long id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    private double price;

    @NotNull(message = "Menu cannot be null.")
    private String menu;
}