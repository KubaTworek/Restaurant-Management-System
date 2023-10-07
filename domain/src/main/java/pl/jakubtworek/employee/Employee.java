package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.List;

class Employee {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private List<SimpleOrder> orders;

    public Employee() {
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getFirstName() {
        return firstName;
    }

    void setFirstName(final String firstName) {
        this.firstName = firstName;
    }

    String getLastName() {
        return lastName;
    }

    void setLastName(final String lastName) {
        this.lastName = lastName;
    }

    Job getJob() {
        return job;
    }

    void setJob(final Job job) {
        this.job = job;
    }

    List<SimpleOrder> getOrders() {
        return orders;
    }

    void setOrders(final List<SimpleOrder> orders) {
        this.orders = orders;
    }

    void add(SimpleOrder order) {
        if (order != null) {
            orders.add(order);
        }
    }
}