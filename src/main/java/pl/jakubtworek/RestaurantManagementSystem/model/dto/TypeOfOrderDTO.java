package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;

public class TypeOfOrderDTO {
    private Long id;
    private String type;
    private List<OrderDTO> orders;

    public TypeOfOrder convertDTOToEntity() {
        return new ModelMapper().map(this, TypeOfOrder.class);
    }
}
