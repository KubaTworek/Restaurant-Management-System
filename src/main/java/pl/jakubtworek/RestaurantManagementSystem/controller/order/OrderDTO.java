package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import javax.validation.constraints.NotNull;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class OrderDTO extends RepresentationModel<OrderDTO> {

    private Long id;

    private double price;

    private String date;

    private String hourOrder;

    private String hourAway;

    @NotNull(message = "Type of order cannot be null.")
    private TypeOfOrderDTO typeOfOrder;

    @NotNull(message = "Menu items cannot be null.")
    private List<MenuItemDTO> menuItems;

    private List<EmployeeDTO> employees;

    public Order convertDTOToEntity() {
        return new ModelMapper().map(this, Order.class);
    }
}