package pl.jakubtworek.employee;

import pl.jakubtworek.employee.dto.Job;
import pl.jakubtworek.employee.dto.SimpleEmployee;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "EMPLOYEES")
public class SqlSimpleEmployee {
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

    public static SqlSimpleEmployee fromEmployee(SimpleEmployee source) {
        SqlSimpleEmployee result = new SqlSimpleEmployee();
        result.id = source.getId();
        result.firstName = source.getFirstName();
        result.lastName = source.getLastName();
        result.job = source.getJob();
        return result;
    }

    public SimpleEmployee toEmployee() {
        return new SimpleEmployee(id, firstName, lastName, job);
    }
}
