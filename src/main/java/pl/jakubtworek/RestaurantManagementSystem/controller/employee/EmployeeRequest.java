package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;

import javax.validation.constraints.NotNull;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
public class EmployeeRequest {

    private Long id;

    @NotNull(message = "First name cannot be null.")
    private String firstName;

    @NotNull(message = "Last name cannot be null.")
    private String lastName;

    @NotNull(message = "Job cannot be null.")
    private String job;
}