package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "MENU_ITEMS")
public class SqlSimpleMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ITEM_ID")
    private Long id;

    @Column(name = "MENU_ITEM_NAME")
    private String name;

    @Column(name = "PRICE")
    private int price;

    @ManyToOne
    @JoinColumn(name = "MENU_ID")
    private SqlMenu menu;

    public static SqlSimpleMenuItem fromMenuItem(SimpleMenuItem source) {
        SqlSimpleMenuItem result = new SqlSimpleMenuItem();
        result.id = source.getId();
        result.name = source.getName();
        result.price = source.getPrice();
        return result;
    }

    public SimpleMenuItem toMenuitem() {
        return new SimpleMenuItem(id, name, price);
    }
}
