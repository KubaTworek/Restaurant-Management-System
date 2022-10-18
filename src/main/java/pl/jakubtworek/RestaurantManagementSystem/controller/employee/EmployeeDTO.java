package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;
import org.modelmapper.ModelMapper;
import org.springframework.hateoas.RepresentationModel;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeDTO extends RepresentationModel<EmployeeDTO> {

    private Long id;

    @NotNull(message = "First name cannot be null.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    private String lastName;

    @NotNull(message = "Job cannot be null.")
    private JobDTO job;
    
    public Employee convertDTOToEntity() {
        return new ModelMapper().map(this, Employee.class);
    }
}