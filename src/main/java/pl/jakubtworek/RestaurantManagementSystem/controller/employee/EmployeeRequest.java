package pl.jakubtworek.RestaurantManagementSystem.controller.employee;

import lombok.*;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeRequest {
    @NotNull(message = "First name cannot be null.")
    private String firstName;
    @NotNull(message = "Last name cannot be null.")
    private String lastName;
    @NotNull(message = "Job cannot be null.")
    private String job;
}