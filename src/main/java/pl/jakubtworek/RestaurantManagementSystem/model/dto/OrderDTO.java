package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeController;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderController;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

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
