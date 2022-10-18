package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuDTO extends RepresentationModel<MenuDTO> {

    private Long id;

    @NotNull(message = "Menu name cannot be null.")
    private String name;

    private List<MenuItemDTO> menuItems;

    public Menu convertDTOToEntity() {
        return new ModelMapper().map(this, Menu.class);
    }
}