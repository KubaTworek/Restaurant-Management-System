package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class MenuRequest {

    @NotNull(message = "Menu name cannot be null.")
    private String name;
}