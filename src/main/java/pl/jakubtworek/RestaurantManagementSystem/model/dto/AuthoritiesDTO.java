package pl.jakubtworek.RestaurantManagementSystem.model.dto;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Authorities;

import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuthoritiesDTO {
    private UUID id;
    private String authority;
    private List<UserDTO> users;

    public Authorities convertDTOToEntity() {
        return new ModelMapper().map(this, Authorities.class);
    }
}
