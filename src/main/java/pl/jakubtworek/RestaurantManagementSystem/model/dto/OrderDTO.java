package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private double price;
    private String date;
    private String hourOrder;
    private String hourAway;
    private TypeOfOrderDTO typeOfOrder;
    private List<MenuItemDTO> menuItems;
    private List<EmployeeDTO> employees;
    private UserDTO userDTO;

    public void add(EmployeeDTO tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(tempEmployee);
    }

    public Order convertDTOToEntity() {
        return new ModelMapper().map(this, Order.class);
    }
    public OrderResponse convertDTOToResponse() {
        return new ModelMapper().map(this, OrderResponse.class);
    }
}
