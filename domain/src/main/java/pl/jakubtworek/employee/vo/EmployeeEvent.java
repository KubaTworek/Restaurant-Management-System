package pl.jakubtworek.employee.vo;

import pl.jakubtworek.DomainEvent;

import java.time.Instant;

public class EmployeeEvent implements DomainEvent {
    private final Instant occurredOn;
    private final Long employeeId;
    private final Long orderId;
    private final Job job;

    public EmployeeEvent(final Long employeeId,
                         final Long orderId,
                         final Job job
    ) {
        this.occurredOn = Instant.now();
        this.employeeId = employeeId;
        this.orderId = orderId;
        this.job = job;
    }

    @Override
    public Instant getOccurredOn() {
        return occurredOn;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Job getJob() {
        return job;
    }
}
