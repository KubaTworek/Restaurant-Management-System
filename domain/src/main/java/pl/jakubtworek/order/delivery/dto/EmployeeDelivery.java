package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.employee.dto.Job;

public class EmployeeDelivery {
    private Long employeeId;
    private Job job;

    public EmployeeDelivery(final Long employeeId, final Job job) {
        this.employeeId = employeeId;
        this.job = job;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Job getJob() {
        return job;
    }
}
