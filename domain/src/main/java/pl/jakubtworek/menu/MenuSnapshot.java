package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItemSnapshot;

import java.util.HashSet;
import java.util.Set;

class MenuSnapshot {
    private Long id;
    private String name;
    private Set<SimpleMenuItemSnapshot> menuItems = new HashSet<>();

    public MenuSnapshot() {
    }

    MenuSnapshot(final Long id, final String name, final Set<SimpleMenuItemSnapshot> menuItems) {
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

    Set<SimpleMenuItemSnapshot> getMenuItems() {
        return menuItems;
    }
}
