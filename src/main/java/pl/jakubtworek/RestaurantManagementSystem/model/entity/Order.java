package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import lombok.*;
import org.hibernate.annotations.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.*;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="orders")
@Entity
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name="price")
    private double price;

    @Column(name="date")
    private String date;

    @Column(name="hour_order")
    private String hourOrder;

    @Column(name="hour_away")
    private String hourAway;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="type_of_order_id")
    @NotNull
    private TypeOfOrder typeOfOrder;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(fetch=FetchType.LAZY, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name="Order_Menu_Item",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="menu_item_id")
    )
    private List<MenuItem> menuItems;

    @LazyCollection(LazyCollectionOption.FALSE)
    @ManyToMany(fetch=FetchType.EAGER, cascade = {CascadeType.MERGE, CascadeType.DETACH})
    @JoinTable(
            name="Order_Employee",
            joinColumns = @JoinColumn(name="order_id"),
            inverseJoinColumns = @JoinColumn(name="employee_id")
    )
    private List<Employee> employees = new ArrayList<>();

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id")
    @NotNull
    private User user;

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

    public OrderDTO convertEntityToDTO() {
        if(employees != null){
            return OrderDTO.builder()
                    .id(this.id)
                    .price(this.price)
                    .date(this.date)
                    .hourOrder(this.hourOrder)
                    .hourAway(this.hourAway)
                    .typeOfOrder(this.typeOfOrder.convertEntityToDTO())
                    .userDTO(this.user.convertEntityToDTO())
                    .menuItems(this.menuItems.stream().map(MenuItem::convertEntityToDTO).collect(Collectors.toList()))
                    .employees(this.employees.stream().map(Employee::convertEntityToDTO).collect(Collectors.toList()))
                    .build();
        } else {
            return OrderDTO.builder()
                    .id(this.id)
                    .price(this.price)
                    .date(this.date)
                    .hourOrder(this.hourOrder)
                    .hourAway(this.hourAway)
                    .typeOfOrder(this.typeOfOrder.convertEntityToDTO())
                    .userDTO(this.user.convertEntityToDTO())
                    .menuItems(this.menuItems.stream().map(MenuItem::convertEntityToDTO).collect(Collectors.toList()))
                    .employees(null)
                    .build();
        }
    }
}