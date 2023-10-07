package pl.jakubtworek.menu.dto;

public class MenuItemDto {

    private Long id;
    private String name;
    private int price;
    private MenuDto menu;

    MenuItemDto() {
    }

    MenuItemDto(final Long id, final String name, final int price, final MenuDto menu) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.menu = menu;
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
        private int price;
        private MenuDto menu;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withName(String name) {
            this.name = name;
            return this;
        }

        public Builder withPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder withMenu(MenuDto menu) {
            this.menu = menu;
            return this;
        }

        public MenuItemDto build() {
            return new MenuItemDto(id, name, price, menu);
        }
    }
}
