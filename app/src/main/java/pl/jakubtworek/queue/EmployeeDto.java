package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.Job;

class EmployeeDto {
    private final Long employeeId;
    private final Job job;

    EmployeeDto(final Long employeeId, final Job job) {
        this.employeeId = employeeId;
        this.job = job;
    }

    Long getEmployeeId() {
        return employeeId;
    }

    Job getJob() {
        return job;
    }
}
