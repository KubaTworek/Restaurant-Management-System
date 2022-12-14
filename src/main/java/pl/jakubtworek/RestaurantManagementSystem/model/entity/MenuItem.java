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
@Table(name = "menu_item")
@Entity
public class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "name")
    @NotNull
    private String name;

    @Column(name = "price")
    @NotNull
    private double price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToMany
    @JoinTable(
            name = "Order_Menu_Item",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<Order> orders;

    public void add(Order tempOrder) {
        if (orders == null) {
            orders = new ArrayList<>();
        }
        if (!orders.contains(tempOrder)) {
            orders.add(tempOrder);
            tempOrder.add(this);
        }
    }

    public void remove() {
        if (orders != null) {
            for (Order o : orders) {
                o.getMenuItems().remove(this);
            }
        }

        if (menu != null) menu.getMenuItems().remove(this);
    }

    public MenuItemDTO convertEntityToDTO() {
        return new ModelMapper().map(this, MenuItemDTO.class);
    }
}