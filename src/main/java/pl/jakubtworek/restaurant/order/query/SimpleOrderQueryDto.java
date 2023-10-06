package pl.jakubtworek.restaurant.order.query;

import lombok.Builder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Builder
@Entity
@Table(name = "orders")
public class SimpleOrderQueryDto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "price")
    private int price;

    @Column(name = "hour_order")
    private ZonedDateTime hourOrder;

    @Column(name = "hour_away")
    private ZonedDateTime hourAway;

    @Column(name = "type_of_order")
    private TypeOfOrder typeOfOrder;

    public SimpleOrderQueryDto() {
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

    public void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }
}
