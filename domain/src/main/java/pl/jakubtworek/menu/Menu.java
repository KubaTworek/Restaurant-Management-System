package pl.jakubtworek.menu;

import pl.jakubtworek.menu.dto.SimpleMenuItem;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Menu {
    static Menu restore(MenuSnapshot snapshot) {
        return new Menu(
                snapshot.getId(),
                snapshot.getName(),
                snapshot.getMenuItems().stream().map(SimpleMenuItem::restore).collect(Collectors.toList())
        );
    }

    private Long id;
    private String name;
    private List<SimpleMenuItem> menuItems = new ArrayList<>();

    public Menu() {
    }

    private Menu(final Long id, final String name, final List<SimpleMenuItem> menuItems) {
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
    }

    MenuSnapshot getSnapshot() {
        return new MenuSnapshot(
                id,
                name,
                menuItems.stream().map(SimpleMenuItem::getSnapshot).collect(Collectors.toSet())
        );
    }

    void updateInfo(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    void updateName(String name) {
        this.name = name;
    }
}