package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.TypeOfOrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name="type_of_order")
@Entity
public class TypeOfOrder {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="type")
    @NotNull
    private String type;

    @OneToMany(mappedBy = "typeOfOrder", cascade = { ALL })
    private List<Order> orders;

    public void add(Order tempOrder) {
        if(orders == null) {
            orders = new ArrayList<>();
        }

        orders.add(tempOrder);
        tempOrder.setTypeOfOrder(this);
    }

    public TypeOfOrderResponse convertEntityToResponse() {
        return new ModelMapper().map(this, TypeOfOrderResponse.class);
    }
    public TypeOfOrderDTO convertEntityToDTO() {
        return new ModelMapper().map(this, TypeOfOrderDTO.class);
    }
}