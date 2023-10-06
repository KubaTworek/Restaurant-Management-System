package pl.jakubtworek.restaurant.employee.query;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Builder
@Entity
@Table(name = "employee")
public class SimpleEmployeeQueryDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Column(name = "job")
    private Job job;

    public SimpleEmployeeQueryDto() {
    }

    public SimpleEmployeeQueryDto(final Long id, final String firstName, final String lastName, final Job job) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.job = job;
    }

    public Long getId() {
        return id;
    }

    public Job getJob() {
        return job;
    }
}
