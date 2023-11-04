package pl.jakubtworek.menu.dto;

public class SimpleMenuItemSnapshot {
    private Long id;
    private String name;
    private int price;

    public SimpleMenuItemSnapshot() {
    }

    public SimpleMenuItemSnapshot(final Long id, final String name, final int price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }
}
