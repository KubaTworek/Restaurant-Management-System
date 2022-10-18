package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class GetMenuItemDTO {

    private Long id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    private double price;

    @NotNull(message = "Menu cannot be null.")
    private MenuDTO menu;

    public MenuItem convertDTOToEntity() {
        return new ModelMapper().map(this, MenuItem.class);
    }
}