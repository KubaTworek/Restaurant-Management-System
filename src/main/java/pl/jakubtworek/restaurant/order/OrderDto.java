package pl.jakubtworek.restaurant.order;

import pl.jakubtworek.restaurant.order.query.TypeOfOrder;

import java.time.ZonedDateTime;

class OrderDto {
    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;

    OrderDto() {
    }

    OrderDto(final Order source) {
        this.id = source.getId();
        this.price = source.getPrice();
        this.hourOrder = source.getHourOrder();
        this.hourAway = source.getHourAway();
        this.typeOfOrder = source.getTypeOfOrder();
    }

    Long getId() {
        return id;
    }

    TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    int getPrice() {
        return price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    ZonedDateTime getHourAway() {
        return hourAway;
    }

    public void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }
}
