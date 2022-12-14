package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private JobDTO job;
    private List<OrderDTO> orders;

    public Employee convertDTOToEntity() {
        return new ModelMapper().map(this, Employee.class);
    }

    public EmployeeResponse convertDTOToResponse() {
        return new ModelMapper().map(this, EmployeeResponse.class);
    }
}
