package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MenuResponse extends RepresentationModel<MenuResponse> {

    private Long id;

    @NotNull(message = "Menu name cannot be null.")
    private String name;

    private List<MenuItemResponse> menuItems;
}