package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class MenuDTO {
    private Long id;
    private String name;
    private List<MenuItemDTO> menuItems;

    public Menu convertDTOToEntity() {
        return new ModelMapper().map(this, Menu.class);
    }
    public MenuResponse convertDTOToResponse() {
        return new ModelMapper().map(this, MenuResponse.class);
    }
}
