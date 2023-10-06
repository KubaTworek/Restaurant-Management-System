package pl.jakubtworek.restaurant.employee;

import pl.jakubtworek.restaurant.order.Order;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "job")
    private Job job;

    @ManyToMany(mappedBy = "employees")
    private List<Order> orders;

    public Employee() {
    }

    public Employee(final EmployeeDto source) {
        this.id = source.getId();
        this.firstName = source.getFirstName();
        this.lastName = source.getLastName();
        this.job = source.getJob();
        this.orders = source.getOrders().stream().map(Order::new).collect(Collectors.toList());
    }

    public Long getId() {
        return id;
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

    public List<Order> getOrders() {
        return orders;
    }
}