package pl.jakubtworek.menu;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

class MenuSnapshot {
    private Long id;
    private String name;
    private Set<MenuItemSnapshot> menuItems = new HashSet<>();

    MenuSnapshot() {
    }

    MenuSnapshot(final Long id,
                 final String name,
                 final Set<MenuItemSnapshot> menuItems
    ) {
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

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuSnapshot that = (MenuSnapshot) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(menuItems, that.menuItems);
    }
}
