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
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "employee")
class SqlEmployee {
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
    private List<SqlSimpleOrder> orders;

    public SqlEmployee() {
    }

    static SqlEmployee fromEmployee(Employee source) {
        SqlEmployee result = new SqlEmployee();
        result.id = source.getId();
        result.firstName = source.getFirstName();
        result.lastName = source.getLastName();
        result.job = source.getJob();
        result.orders = source.getOrders().stream().map(SqlSimpleOrder::fromOrder).collect(Collectors.toList());
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
