package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Set;

class EmployeeSnapshot {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private Set<OrderId> orders = new HashSet<>();

    public EmployeeSnapshot() {
    }

    EmployeeSnapshot(final Long id,
                     final String firstName,
                     final String lastName,
                     final Job job,
                     final Set<OrderId> orders
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
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

    Set<OrderId> getOrders() {
        return orders;
    }
}
