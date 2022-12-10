package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class TypeOfOrderDTO {
    private UUID id;
    private String type;
    private List<OrderDTO> orders;

    public TypeOfOrder convertDTOToEntity() {
        return new ModelMapper().map(this, TypeOfOrder.class);
    }
}
