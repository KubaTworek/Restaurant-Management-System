package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;

public class MenuItemDTO {
    private Long id;
    private String name;
    private double price;
    private MenuDTO menu;
    private List<OrderDTO> orders;
}
