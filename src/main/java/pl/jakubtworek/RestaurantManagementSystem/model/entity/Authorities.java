package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import lombok.*;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="authorities")
@Entity
public class Authorities {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name = "authority", nullable = false)
    private String authority;

    @OneToMany(mappedBy = "authorities")
    private List<User> users;
}
