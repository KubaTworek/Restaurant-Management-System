package pl.jakubtworek.restaurant.menu;

import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.List;

@Entity
@Table(name = "menu_item")
class MenuItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.DETACH})
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToMany
    @JoinTable(
            name = "Order_Menu_Item",
            joinColumns = @JoinColumn(name = "menu_item_id"),
            inverseJoinColumns = @JoinColumn(name = "order_id")
    )
    private List<SimpleOrderQueryDto> orders;

    public MenuItem() {
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    int getPrice() {
        return price;
    }

    Menu getMenu() {
        return menu;
    }

    void setMenu(final Menu menu) {
        this.menu = menu;
    }

    List<SimpleOrderQueryDto> getOrders() {
        return orders;
    }
}