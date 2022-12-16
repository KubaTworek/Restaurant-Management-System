package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "type_of_order")
@Entity
public class TypeOfOrder {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private UUID id;

    @Column(name = "type")
    @NotNull
    private String type;

    @OneToMany(mappedBy = "typeOfOrder", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<Order> orders;

    public void add(Order tempOrder) {
        if (orders == null) {
            orders = new ArrayList<>();
        }

        orders.add(tempOrder);
        tempOrder.setTypeOfOrder(this);
    }

    public TypeOfOrderDTO convertEntityToDTO() {
        return new ModelMapper().map(this, TypeOfOrderDTO.class);
    }
}