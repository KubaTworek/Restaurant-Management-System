package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.user.UserResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String password;
    private Authorities authorities;
    private List<OrderDTO> orders;

    public void add(OrderDTO tempOrder) {
        if(orders == null) {
            orders = new ArrayList<>();
        }
        if(!orders.contains(tempOrder)){
            orders.add(tempOrder);
        }
    }

    public User convertDTOToEntity() {
        return new ModelMapper().map(this, User.class);
    }
    public UserResponse convertDTOToResponse() {
        return new ModelMapper().map(this, UserResponse.class);
    }
}
