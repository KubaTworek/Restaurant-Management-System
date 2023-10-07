package pl.jakubtworek.order.dto;

import java.time.ZonedDateTime;

public class OrderDto {

    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;

    OrderDto() {
    }

    OrderDto(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
    }

    static public Builder builder() {
        return new Builder();
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

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    public static class Builder {
        private Long id;
        private int price;
        private ZonedDateTime hourOrder;
        private ZonedDateTime hourAway;
        private TypeOfOrder typeOfOrder;

        public Builder withId(Long id) {
            this.id = id;
            return this;
        }

        public Builder withPrice(int price) {
            this.price = price;
            return this;
        }

        public Builder withHourOrder(ZonedDateTime hourOrder) {
            this.hourOrder = hourOrder;
            return this;
        }

        public Builder withHourAway(ZonedDateTime hourAway) {
            this.hourAway = hourAway;
            return this;
        }

        public Builder withTypeOfOrder(TypeOfOrder typeOfOrder) {
            this.typeOfOrder = typeOfOrder;
            return this;
        }

        public OrderDto build() {
            return new OrderDto(id, price, hourOrder, hourAway, typeOfOrder);
        }
    }
}
