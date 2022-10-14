package pl.jakubtworek.RestaurantManagementSystem.entity;


import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Table(name="Type_Of_Order")
@Component
public class TypeOfOrder {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private int id;

    @Column(name="type")
    private String type;

    @OneToMany(mappedBy = "typeOfOrder", cascade = { ALL })
    @JsonManagedReference(value="type_of_order_id")
    private List<Order> orders;

    public TypeOfOrder() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
        tempOrder.setTypeOfOrder(this);
    }

    @Override
    public String toString() {
        return "TypeOfOrder{" +
                "id=" + id +
                ", type='" + type + '\'' +
                ", orders=" + orders +
                '}';
    }
}
