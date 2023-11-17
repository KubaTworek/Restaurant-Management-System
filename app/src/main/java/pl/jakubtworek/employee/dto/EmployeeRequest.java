package pl.jakubtworek.employee.dto;

import jakarta.validation.constraints.NotBlank;

public record EmployeeRequest(
        @NotBlank(message = "First name cannot be null.") String firstName,
        @NotBlank(message = "Last name cannot be null.") String lastName,
        @NotBlank(message = "Job cannot be null.") String job
) { }
