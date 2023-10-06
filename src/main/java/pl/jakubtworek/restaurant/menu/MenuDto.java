package pl.jakubtworek.restaurant.menu;

import java.util.List;

class MenuDto {
    private Long id;
    private String name;
    private List<MenuItemDto> menuItems;

    public MenuDto() {
    }

    Long getId() {
        return id;
    }
}
