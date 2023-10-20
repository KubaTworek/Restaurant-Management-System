package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.order.SqlSimpleOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "EMPLOYEES")
class SqlEmployee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "EMPLOYEE_ID")
    private Long id;

    @Column(name = "FIRST_NAME")
    private String firstName;

    @Column(name = "LAST_NAME")
    private String lastName;

    @Column(name = "JOB")
    private Job job;

    @ManyToMany(mappedBy = "employees")
    private Set<SqlSimpleOrder> orders;

    public SqlEmployee() {
    }

    static SqlEmployee fromEmployee(Employee source) {
        SqlEmployee result = new SqlEmployee();
        result.id = source.getId();
        result.firstName = source.getFirstName();
        result.lastName = source.getLastName();
        result.job = source.getJob();
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toSet());
        return result;
    }

    Employee toEmployee() {
        Employee result = new Employee();
        result.setId(id);
        result.setFirstName(firstName);
        result.setLastName(lastName);
        result.setJob(job);
        result.setOrders(orders.stream().map(SqlSimpleOrder::toOrder).collect(Collectors.toList()));
        return result;
    }
}
