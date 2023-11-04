package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Employee {
    static Employee restore(EmployeeSnapshot snapshot) {
        return new Employee(
                snapshot.getId(),
                snapshot.getFirstName(),
                snapshot.getLastName(),
                snapshot.getJob(),
                snapshot.getOrders().stream().map(SimpleOrder::restore).collect(Collectors.toList())
        );
    }

    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private List<SimpleOrder> orders = new ArrayList<>();

    public Employee() {
    }

    private Employee(final Long id, final String firstName, final String lastName, final Job job, final List<SimpleOrder> orders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.orders = orders;
    }

    EmployeeSnapshot getSnapshot() {
        return new EmployeeSnapshot(
                id,
                firstName,
                lastName,
                job,
                orders.stream().map(SimpleOrder::getSnapshot).collect(Collectors.toSet())
        );
    }

    void updateInfo(String firstName, String lastName, String jobName) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = getAndValidateJob(jobName);
    }

    private Job getAndValidateJob(String jobName) {
        try {
            return Job.valueOf(jobName);
        } catch (IllegalArgumentException e) {
            throw new IllegalStateException("Invalid job type!!");
        }
    }
}