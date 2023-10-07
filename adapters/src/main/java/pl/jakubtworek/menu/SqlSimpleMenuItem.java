package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItem;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "menu_item")
public class SqlSimpleMenuItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private int price;

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
