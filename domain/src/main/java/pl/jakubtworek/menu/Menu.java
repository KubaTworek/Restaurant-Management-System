package pl.jakubtworek.menu;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

class Menu {
    private Long id;
    private String name;
    private Set<MenuItem> menuItems = new HashSet<>();

    Menu() {
    }

    private Menu(final Long id, final String name, final Set<MenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
    }

    static Menu restore(MenuSnapshot snapshot, int depth) {
        if (depth <= 0) {
            return new Menu(
                    snapshot.getId(),
                    snapshot.getName(),
                    Collections.emptySet()
            );
        }
        return new Menu(
                snapshot.getId(),
                snapshot.getName(),
                snapshot.getMenuItems().stream().map(mi -> MenuItem.restore(mi, depth - 1)).collect(Collectors.toSet())
        );
    }

    MenuSnapshot getSnapshot(int depth) {
        if (depth <= 0) {
            return new MenuSnapshot(id, name, Collections.emptySet());
        }

        return new MenuSnapshot(
                id,
                name,
                menuItems.stream().map(item -> item.getSnapshot(depth - 1)).collect(Collectors.toSet())
        );
    }

    void updateInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    void updateName(String name) {
        this.name = name;
    }

    void addMenuItem(final MenuItem menuItem) {
        if (menuItem != null) {
            this.menuItems.add(menuItem);
        }
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final Menu menu = (Menu) o;
        return Objects.equals(id, menu.id) && Objects.equals(name, menu.name) && Objects.equals(menuItems, menu.menuItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, menuItems);
    }
}
