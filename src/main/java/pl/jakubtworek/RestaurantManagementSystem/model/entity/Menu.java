package pl.jakubtworek.RestaurantManagementSystem.model.entity;

import com.sun.istack.NotNull;
import lombok.*;
import org.modelmapper.ModelMapper;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.MenuDTO;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static javax.persistence.CascadeType.ALL;
import static javax.persistence.CascadeType.REMOVE;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="menu")
@Entity
public class Menu {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Column(name="name")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "menu", cascade = { REMOVE, ALL })
    private List<MenuItem> menuItems;

    public void add(MenuItem tempMenuItem) {
        if(menuItems == null) {
            menuItems = new ArrayList<>();
        }

        menuItems.add(tempMenuItem);
        tempMenuItem.setMenu(this);
    }

    public MenuDTO convertEntityToDTO() {
        return new ModelMapper().map(this, MenuDTO.class);
    }
    public MenuResponse convertEntityToResponse() {
        return new ModelMapper().map(this, MenuResponse.class);
    }
}