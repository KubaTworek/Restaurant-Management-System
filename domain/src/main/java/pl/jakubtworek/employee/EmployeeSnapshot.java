package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.dto.Status;
import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Set;

class EmployeeSnapshot {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private Status status;
    private Set<OrderId> orders = new HashSet<>();

    public EmployeeSnapshot() {
    }

    EmployeeSnapshot(final Long id,
                     final String firstName,
                     final String lastName,
                     final Job job,
                     final Status status,
                     final Set<OrderId> orders
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.status = status;
        this.orders = orders;
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

    Job getJob() {
        return job;
    }

    Status getStatus() {
        return status;
    }

    Set<OrderId> getOrders() {
        return orders;
    }
}
