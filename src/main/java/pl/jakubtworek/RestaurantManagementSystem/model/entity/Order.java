package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="orders")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="price")
    @NotNull
    private double price;

    @Column(name="date")
    @NotNull
    private String date;

    @Column(name="hour_order")
    @NotNull
    private String hourOrder;

    @Column(name="hour_away")
    private String hourAway;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name="type_of_order_id")
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

    @ManyToMany(fetch=FetchType.LAZY,
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

        menuItems.add(tempMenuItem);
        tempMenuItem.add(this);
    }

    public void add(Employee tempEmployee) {
        if(employees == null) {
            employees = new ArrayList<>();
        }

        employees.add(tempEmployee);
        tempEmployee.add(this);
    }

    public OrderDTO convertEntityToDTO() {
        return new ModelMapper().map(this, OrderDTO.class);
    }
}