package pl.jakubtworek.employee.dto;

import javax.validation.constraints.NotNull;

public class EmployeeRequest {
    @NotNull(message = "First name cannot be null.")
    private final String firstName;
    @NotNull(message = "Last name cannot be null.")
    private final String lastName;
    @NotNull(message = "Job cannot be null.")
    private final String job;

    public EmployeeRequest(final String firstName, final String lastName, final String job) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

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