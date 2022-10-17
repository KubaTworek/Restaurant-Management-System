package pl.jakubtworek.RestaurantManagementSystem.controller.menu;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class MenuItemDTO {

    private Long id;

    @NotNull(message = "Name cannot be null.")
    private String name;

    @NotNull(message = "Price cannot be null.")
    private double price;

    @JsonIgnore
    private Menu menu;

    public MenuItem convertDTOToEntity() {
        return new ModelMapper().map(this, MenuItem.class);
    }
}