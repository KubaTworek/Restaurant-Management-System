package pl.jakubtworek.restaurant.employee;

import pl.jakubtworek.restaurant.order.OrderDto;

import java.util.List;
import java.util.stream.Collectors;

public class EmployeeDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Job job;
    private List<OrderDto> orders;

    EmployeeDto() {
    }

    public EmployeeDto(final Employee source) {
        this.id = source.getId();
        this.firstName = source.getFirstName();
        this.lastName = source.getLastName();
        this.job = source.getJob();
        this.orders = source.getOrders().stream().map(OrderDto::new).collect(Collectors.toList());
    }

    Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }

    public List<OrderDto> getOrders() {
        return orders;
    }

    String getFirstName() {
        return firstName;
    }

    String getLastName() {
        return lastName;
    }

    public void add(OrderDto order) {
        if (order != null) {
            orders.add(order);
            order.getEmployees().add(this);
        }
    }

}
