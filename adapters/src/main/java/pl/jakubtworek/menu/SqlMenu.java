package pl.jakubtworek.menu;

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
@Table(name = "MENU")
class SqlMenu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "MENU_ID")
    private Long id;

    @Column(name = "MENU_NAME")
    private String name;

    @OneToMany(mappedBy = "menu", cascade = CascadeType.MERGE)
    private List<SqlSimpleMenuItem> menuItems;

    public SqlMenu() {
    }

    static SqlMenu fromMenu(Menu source) {
        SqlMenu result = new SqlMenu();
        result.id = source.getId();
        result.name = source.getName();
        result.menuItems = source.getMenuItems().stream().map(SqlSimpleMenuItem::fromMenuItem).collect(Collectors.toList());
        return result;
    }

    Menu toMenu() {
        Menu result = new Menu();
        result.setId(id);
        result.setName(name);
        result.setMenuItems(menuItems.stream().map(SqlSimpleMenuItem::toMenuitem).collect(Collectors.toList()));
        return result;
    }
}
