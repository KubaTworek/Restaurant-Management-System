package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @NotNull
    @Column(name="first_name")
    private String firstName;

    @NotNull
    @Column(name="last_name")
    private String lastName;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="job_id")
    private Job job;

    @ManyToMany(mappedBy = "employees")
    private List<Order> orders;

    public void add(Order tempOrder) {
        if(orders == null) {
            orders = new ArrayList<>();
        }
        if(!orders.contains(tempOrder)){
            orders.add(tempOrder);
            tempOrder.add(this);
        }
    }

    public void remove(Order tempOrder){
        orders.remove(tempOrder);
        tempOrder.getEmployees().remove(this);
    }

    public EmployeeDTO convertEntityToDTO() {
        return new ModelMapper().map(this, EmployeeDTO.class);
    }
}