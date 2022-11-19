package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="employee")
@Entity
public class Employee {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull
    @Column(name="first_name")
    private String firstName;

    @NotNull
    @Column(name="last_name")
    private String lastName;

    @NotNull
    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="job_id")
    private Job job;

    @ManyToMany(fetch=FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="Order_Employee",
            joinColumns = @JoinColumn(name="employee_id"),
            inverseJoinColumns = @JoinColumn(name="order_id")
    )
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

    public EmployeeDTO convertEntityToDTO() {
        return new ModelMapper().map(this, EmployeeDTO.class);
    }
}