package pl.jakubtworek.restaurant.menu;

class MenuItemDto {
    private Long id;
    private String name;
    private int price;
    private MenuDto menu;

    MenuItemDto() {
    }

    public MenuItemDto(final MenuItem source) {
        this.id = source.getId();
        this.name = source.getName();
        this.price = source.getPrice();
        this.menu = new MenuDto(source.getMenu());
    }

    Long getId() {
        return id;
    }
}
