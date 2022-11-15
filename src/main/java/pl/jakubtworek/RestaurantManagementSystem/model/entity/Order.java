package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Table(name="orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="price")
    private double price;

    @Column(name="date")
    private String date;

    @Column(name="hour_order")
    private String hourOrder;

    @Column(name="hour_away")
    private String hourAway;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="type_of_order_id")
    @NotNull
    private TypeOfOrder typeOfOrder;

    @ManyToMany(fetch=FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="Order_Menu_Item",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="menu_item_id")
    )
    private List<MenuItem> menuItems;

    @ManyToMany(fetch=FetchType.EAGER,
            cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                    CascadeType.DETACH, CascadeType.REFRESH})
    @JoinTable(
            name="Order_Employee",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="employee_id")
    )
    private List<Employee> employees;

    public void add(MenuItem tempMenuItem) {
        if(menuItems == null) {
            menuItems = new ArrayList<>();
        }
        if(!menuItems.contains(tempMenuItem)){
            menuItems.add(tempMenuItem);
            tempMenuItem.add(this);
        }
    }

    public void add(Employee tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }
        if(!employees.contains(tempEmployee)){
            employees.add(tempEmployee);
            tempEmployee.add(this);
        }
    }

    public OrderResponse convertEntityToResponse() {
        return new ModelMapper().map(this, OrderResponse.class);
    }
    public OrderDTO convertEntityToDTO() {
        return new ModelMapper().map(this, OrderDTO.class);
    }
}