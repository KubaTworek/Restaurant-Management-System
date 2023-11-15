package pl.jakubtworek.order.delivery.dto;

import pl.jakubtworek.employee.vo.Job;

public record EmployeeDelivery(
        Long employeeId,
        Job job
) {
}
