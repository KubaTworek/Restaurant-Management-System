package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuItemDTO;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="menu_item")
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @Column(name="price")
    @NotNull
    private double price;

    @ManyToOne(fetch=FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToMany(fetch=FetchType.EAGER)
    @JoinTable(
            name="Order_Menu_Item",
            joinColumns = @JoinColumn(name="menu_item_id"),
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

    public MenuItemDTO convertEntityToDTO() {
        return new ModelMapper().map(this, MenuItemDTO.class);
    }
}