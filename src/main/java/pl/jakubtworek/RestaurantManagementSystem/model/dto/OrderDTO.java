package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
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
    private TypeOfOrder typeOfOrder;
    private List<MenuItem> menuItems;
    private List<Employee> employees;

    public void add(Employee tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }
        employees.add(tempEmployee);
    }

    public Order convertDTOToEntity() {
        return new ModelMapper().map(this, Order.class);
    }
}
