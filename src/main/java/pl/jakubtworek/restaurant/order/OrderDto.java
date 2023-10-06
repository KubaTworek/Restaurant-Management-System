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

    Long getId() {
        return id;
    }

    int getPrice() {
        return price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }
}
