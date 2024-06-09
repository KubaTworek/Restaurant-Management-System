package pl.jakubtworek.employee;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.order.vo.OrderDeliveryId;
import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class EmployeeSnapshot {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private Status status;
    private Set<OrderId> orders = new HashSet<>();
    private Set<OrderDeliveryId> deliveries = new HashSet<>();

    EmployeeSnapshot() {
    }

    EmployeeSnapshot(final Long id,
                     final String firstName,
                     final String lastName,
                     final Job job,
                     final Status status,
                     final Set<OrderId> orders,
                     final Set<OrderDeliveryId> deliveries
    ) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.status = status;
        this.orders = orders;
        this.deliveries = deliveries;
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

    Set<OrderDeliveryId> getDeliveries() {
        return deliveries;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final EmployeeSnapshot that = (EmployeeSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && job == that.job && status == that.status && Objects.equals(orders, that.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, job, status, orders);
    }
}
