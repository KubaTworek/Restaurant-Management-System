package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="job")
@Entity
public class Job {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "job", cascade = { ALL })
    private List<Employee> employees;

    public void add(Employee tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }

        employees.add(tempEmployee);
        tempEmployee.setJob(this);
    }
}