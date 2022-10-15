package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import javax.validation.constraints.NotNull;
import java.util.List;

public class OrderDTO extends RepresentationModel<OrderDTO> {

    private Long id;

    @NotNull(message = "Price of order cannot be null.")
    private double price;

    @NotNull(message = "Date of order cannot be null.")
    private String date;

    @NotNull(message = "Hour of order cannot be null.")
    private String hourOrder;

    private String hourAway;

    @NotNull(message = "Type of order cannot be null.")
    private TypeOfOrder typeOfOrder;

    private List<MenuItem> menuItems;

    private List<Employee> employees;

    public OrderDTO convertDTOToEntity() {
        return new ModelMapper().map(this, OrderDTO.class);
    }
}