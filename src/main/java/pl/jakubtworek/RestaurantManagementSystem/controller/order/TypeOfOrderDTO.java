package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class TypeOfOrderDTO {

    private Long id;

    @NotNull(message = "Type of order cannot be null.")
    private String type;

    public TypeOfOrder convertDTOToEntity() {
        return new ModelMapper().map(this, TypeOfOrder.class);
    }
}
