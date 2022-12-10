package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;

import java.util.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse {

    private UUID id;
    private String name;
    private List<MenuItemResponse> menuItems;
}