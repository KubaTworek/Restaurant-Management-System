package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Job;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private List<OrderDTO> orders;

    public Employee convertDTOToEntity() {
        return new ModelMapper().map(this, Employee.class);
    }
    public EmployeeResponse convertDTOToResponse() {
        return new ModelMapper().map(this, EmployeeResponse.class);
    }
}
