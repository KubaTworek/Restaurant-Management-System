package pl.jakubtworek.order;

import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.ZonedDateTime;

@Entity
@Table(name = "orders")
public class SqlSimpleOrder {
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

    public static SqlSimpleOrder fromOrder(SimpleOrder source) {
        SqlSimpleOrder result = new SqlSimpleOrder();
        result.id = source.getId();
        result.price = source.getPrice();
        result.hourOrder = source.getHourOrder();
        result.hourAway = source.getHourAway();
        result.typeOfOrder = source.getTypeOfOrder();
        return result;
    }

    public SimpleOrder toOrder() {
        return new SimpleOrder(id, price, hourOrder, hourAway, typeOfOrder);
    }
}
