package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemResponse {
    private Long id;
    private String name;
    private double price;
}