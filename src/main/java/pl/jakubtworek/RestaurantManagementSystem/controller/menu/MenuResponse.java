package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuResponse extends RepresentationModel<MenuResponse> {

    private Long id;

    @NotNull(message = "Menu name cannot be null.")
    private String name;

    private List<MenuItemResponse> menuItems;
}