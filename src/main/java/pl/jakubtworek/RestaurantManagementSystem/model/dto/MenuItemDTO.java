package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuItemDTO {
    private Long id;
    private String name;
    private double price;
    private MenuDTO menu;
    private List<OrderDTO> orders;

    public MenuItem convertDTOToEntity() {
        return new ModelMapper().map(this, MenuItem.class);
    }
    public MenuItemResponse convertDTOToResponse() {
        return new ModelMapper().map(this, MenuItemResponse.class);
    }
}
