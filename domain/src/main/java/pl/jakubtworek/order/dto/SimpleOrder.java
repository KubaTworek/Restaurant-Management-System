package pl.jakubtworek.order.dto;

import java.time.ZonedDateTime;

public class SimpleOrder {
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

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(final int price) {
        this.price = price;
    }

    public ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    public void setHourOrder(final ZonedDateTime hourOrder) {
        this.hourOrder = hourOrder;
    }

    public ZonedDateTime getHourAway() {
        return hourAway;
    }

    public void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    public void setTypeOfOrder(final TypeOfOrder typeOfOrder) {
        this.typeOfOrder = typeOfOrder;
    }
}
