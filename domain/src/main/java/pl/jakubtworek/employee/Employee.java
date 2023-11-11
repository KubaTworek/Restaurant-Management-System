package pl.jakubtworek.employee;

import pl.jakubtworek.DomainEvent;
import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.OrderId;

import java.util.HashSet;
import java.util.Set;

class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private Set<OrderId> orders = new HashSet<>();

    public Employee() {
    }

    private Employee(final Long id, final String firstName, final String lastName, final Job job, final Set<OrderId> orders) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
        this.orders = orders;
    }

    static Employee restore(EmployeeSnapshot snapshot) {
        return new Employee(
                snapshot.getId(),
                snapshot.getFirstName(),
                snapshot.getLastName(),
                snapshot.getJob(),
                snapshot.getOrders()
        );
    }

    EmployeeSnapshot getSnapshot() {
        return new EmployeeSnapshot(
                id,
                firstName,
                lastName,
                job,
                orders
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

    DomainEvent deliveredOrderWithId(final Order order) {
        return new OrderEvent(
                order.getOrderId(),
                this.id,
                order.getOrderType(),
                order.getAmountOfMenuItems(),
                OrderEvent.State.DELIVERED
        );
    }
}