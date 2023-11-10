package pl.jakubtworek.menu;

import java.util.HashSet;
import java.util.Set;

class MenuSnapshot {
    private Long id;
    private String name;
    private Set<MenuItemSnapshot> menuItems = new HashSet<>();

    public MenuSnapshot() {
    }

    MenuSnapshot(final Long id, final String name, final Set<MenuItemSnapshot> menuItems) {
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    Set<MenuItemSnapshot> getMenuItems() {
        return menuItems;
    }
}
