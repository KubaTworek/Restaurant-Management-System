package pl.jakubtworek.menu.dto;

import java.util.List;

public class MenuDto {

    private Long id;
    private String name;
    private List<MenuItemDto> menuItems;

    public MenuDto() {
    }

    MenuDto(final Long id, final String name, final List<MenuItemDto> menuItems) {
        this.id = id;
        this.name = name;
        this.menuItems = menuItems;
    }

    static public Builder builder() {
        return new Builder();
    }

    public Long getId() {
        return id;
    }

    public static class Builder {
        private Long id;
        private String name;
        private List<MenuItemDto> menuItems;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withMenuItems(List<MenuItemDto> menuItems) {
            this.menuItems = menuItems;
            return this;
        }

        public MenuDto build() {
            return new MenuDto(id, name, menuItems);
        }
    }
}
