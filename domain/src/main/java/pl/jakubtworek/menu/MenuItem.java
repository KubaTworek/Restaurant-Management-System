package pl.jakubtworek.menu;

import pl.jakubtworek.order.dto.SimpleOrder;

import java.util.List;

class MenuItem {
    private Long id;
    private String name;
    private int price;
    private Menu menu;
    private List<SimpleOrder> orders;

    public MenuItem() {
    }

    Long getId() {
        return id;
    }

    void setId(final Long id) {
        this.id = id;
    }

    String getName() {
        return name;
    }

    void setName(final String name) {
        this.name = name;
    }

    int getPrice() {
        return price;
    }

    void setPrice(final int price) {
        this.price = price;
    }

    Menu getMenu() {
        return menu;
    }

    void setMenu(final Menu menu) {
        this.menu = menu;
    }

    List<SimpleOrder> getOrders() {
        return orders;
    }

    void setOrders(final List<SimpleOrder> orders) {
        this.orders = orders;
    }
}