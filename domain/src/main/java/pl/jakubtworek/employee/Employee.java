package pl.jakubtworek.employee;

import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.employee.vo.Job;
import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private Status status;
    private Set<OrderId> orders = new HashSet<>();

    Employee() {
    }

    private Employee(final Long id,
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

    static Employee restore(EmployeeSnapshot snapshot) {
        return new Employee(
                snapshot.getId(),
                snapshot.getFirstName(),
                snapshot.getLastName(),
                snapshot.getJob(),
                snapshot.getStatus(),
                snapshot.getOrders()
        );
    }

    EmployeeSnapshot getSnapshot() {
        return new EmployeeSnapshot(
                id,
                firstName,
                lastName,
                job,
                status,
                orders
        );
    }

    void updateInfo(String firstName, String lastName, String jobName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = getAndValidateJob(jobName);
        this.status = Status.ACTIVE;
    }

    private Job getAndValidateJob(String jobName) {
        try {
            return Job.valueOf(jobName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid job type!!");
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Employee employee = (Employee) o;
        return Objects.equals(id, employee.id) && Objects.equals(firstName, employee.firstName) && Objects.equals(lastName, employee.lastName) && job == employee.job && status == employee.status && Objects.equals(orders, employee.orders);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, firstName, lastName, job, status, orders);
    }
}
