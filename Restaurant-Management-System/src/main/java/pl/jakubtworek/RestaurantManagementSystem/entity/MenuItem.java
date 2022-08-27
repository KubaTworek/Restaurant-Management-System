package pl.jakubtworek.RestaurantManagementSystem.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="Menu_Item")
@Component
public class MenuItem {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="name")
    private String name;

    @Column(name="price")
    private double price;

    @ManyToOne(fetch=FetchType.LAZY, cascade = CascadeType.REFRESH)
    @JoinColumn(name="menu_id")
    @JsonBackReference(value="menu_id")
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

    public MenuItem() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public void add(Order tempOrder) {
        if(orders == null) {
            orders = new ArrayList<>();
        }

        orders.add(tempOrder);
        tempOrder.add(this);
    }

    @Override
    public String toString() {
        return "MenuItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", menu=" + menu +
                '}';
    }
}
