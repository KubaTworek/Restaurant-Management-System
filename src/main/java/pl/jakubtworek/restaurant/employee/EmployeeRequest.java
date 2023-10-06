package pl.jakubtworek.restaurant.employee;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Getter
@NoArgsConstructor
@AllArgsConstructor
class EmployeeRequest {
    @NotNull(message = "First name cannot be null.")
    private String firstName;
    @NotNull(message = "Last name cannot be null.")
    private String lastName;
    @NotNull(message = "Job cannot be null.")
    private String job;
}