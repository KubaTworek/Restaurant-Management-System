package pl.jakubtworek.restaurant.menu;

class MenuItemDto {
    private Long id;
    private String name;
    private int price;
    private MenuDto menu;

    MenuItemDto() {
    }

    Long getId() {
        return id;
    }
}
