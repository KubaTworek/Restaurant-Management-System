package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.List;

class Menu {
    private Long id;
    private String name;
    private List<SimpleMenuItem> menuItems;

    public Menu() {
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    List<SimpleMenuItem> getMenuItems() {
        return menuItems;
    }

    void setMenuItems(final List<SimpleMenuItem> menuItems) {
        this.menuItems = menuItems;
    }
}