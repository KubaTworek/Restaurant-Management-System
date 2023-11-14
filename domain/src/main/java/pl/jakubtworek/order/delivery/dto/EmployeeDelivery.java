package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.employee.dto.Job;

public record EmployeeDelivery(
        Long employeeId,
        Job job
) {}
