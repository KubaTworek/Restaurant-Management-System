package pl.jakubtworek.restaurant.menu;

import java.util.List;
import java.util.stream.Collectors;

public class MenuDto {
    private Long id;
    private String name;
    private List<MenuItemDto> menuItems;

    MenuDto() {
    }

    MenuDto(final Menu source) {
        this.id = source.getId();
        this.name = source.getName();
        this.menuItems = source.getMenuItems().stream().map(MenuItemDto::new).collect(Collectors.toList());
    }

    Long getId() {
        return id;
    }

    String getName() {
        return name;
    }

    List<MenuItemDto> getMenuItems() {
        return menuItems;
    }
}
