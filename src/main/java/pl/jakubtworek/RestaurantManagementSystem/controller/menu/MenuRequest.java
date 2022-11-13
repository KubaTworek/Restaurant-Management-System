package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuRequest {

    private Long id;

    @NotNull(message = "Menu name cannot be null.")
    private String name;
}