package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeResponse extends RepresentationModel<EmployeeResponse> {

    private Long id;

    @NotNull(message = "First name cannot be null.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    private String lastName;

    @NotNull(message = "Job cannot be null.")
    private JobDTO job;
}