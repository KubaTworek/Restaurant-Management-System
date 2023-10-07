package pl.jakubtworek.employee.dto;

import javax.validation.constraints.NotNull;

public class EmployeeRequest {
    @NotNull(message = "First name cannot be null.")
    private String firstName;
    @NotNull(message = "Last name cannot be null.")
    private String lastName;
    @NotNull(message = "Job cannot be null.")
    private String job;

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getJob() {
        return job;
    }
}