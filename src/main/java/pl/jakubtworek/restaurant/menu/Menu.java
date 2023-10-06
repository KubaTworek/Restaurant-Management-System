package pl.jakubtworek.restaurant.menu;

import com.sun.istack.NotNull;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@Table(name = "menu")
public class Menu {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    @NotNull
    private String name;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuItem> menuItems;

    public Menu() {
    }

    public Menu(final MenuDto source) {
        this.id = source.getId();
        this.name = source.getName();
        this.menuItems = source.getMenuItems().stream().map(MenuItem::new).collect(Collectors.toList());
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    List<MenuItem> getMenuItems() {
        return menuItems;
    }
}