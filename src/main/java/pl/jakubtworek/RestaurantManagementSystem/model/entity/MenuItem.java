package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="menu_item")
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
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

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name="menu_id")
    private Menu menu;

    @ManyToMany(fetch=FetchType.LAZY,
                cascade = {CascadeType.PERSIST, CascadeType.MERGE,
                        CascadeType.DETACH, CascadeType.REFRESH})
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