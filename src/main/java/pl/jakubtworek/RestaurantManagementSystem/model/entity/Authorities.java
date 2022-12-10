package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import lombok.*;

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

    @OneToMany(mappedBy = "authorities")
    private List<User> users;
}
