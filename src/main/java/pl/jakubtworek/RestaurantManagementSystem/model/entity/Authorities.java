package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.AuthoritiesDTO;

import javax.persistence.*;
import java.util.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="authorities")
@Entity
public class Authorities {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name="id")
    private UUID id;

    @Column(name = "authority", nullable = false)
    private String authority;

    @OneToMany(mappedBy = "authorities", cascade = {CascadeType.REMOVE, CascadeType.DETACH})
    private List<User> users;

    public AuthoritiesDTO convertEntityToDTO() {
        return new ModelMapper().map(this, AuthoritiesDTO.class);
    }
}
