package pl.jakubtworek.restaurant.employee;

import pl.jakubtworek.restaurant.employee.query.Job;

class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;

    EmployeeDto() {
    }

    public EmployeeDto(final Employee source) {
        this.id = source.getId();
        this.firstName = source.getFirstName();
        this.lastName = source.getLastName();
        this.job = source.getJob();
    }

    Long getId() {
        return id;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    public Job getJob() {
        return job;
    }
}
