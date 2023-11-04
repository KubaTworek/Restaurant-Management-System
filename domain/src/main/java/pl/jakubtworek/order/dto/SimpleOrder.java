package pl.jakubtworek.order.dto;

import java.time.ZonedDateTime;

public class SimpleOrder {
    public static SimpleOrder restore(final SimpleOrderSnapshot snapshot) {
        return new SimpleOrder(snapshot.getId(), snapshot.getPrice(), snapshot.getHourOrder(), snapshot.getHourAway(), snapshot.getTypeOfOrder());
    }

    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;

    public SimpleOrder(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
    }

    public SimpleOrderSnapshot getSnapshot() {
        return new SimpleOrderSnapshot(id, price, hourOrder, hourAway, typeOfOrder);
    }

    public Long getId() {
        return id;
    }

    public int getPrice() {
        return price;
    }

    public ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    public ZonedDateTime getHourAway() {
        return hourAway;
    }

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }
}
