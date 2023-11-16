package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(as = OrderItemDto.DeserializationImpl.class)
public interface OrderItemDto {

    static OrderItemDto create(final Long id,
                               final String name,
                               final BigDecimal price,
                               final Integer amount
    ) {
        return new OrderItemDto.DeserializationImpl(id, name, price, amount);
    }

    Long getId();
    String getName();
    BigDecimal getPrice();
    Integer getAmount();

    class DeserializationImpl implements OrderItemDto {
        private final Long id;
        private final String name;
        private final BigDecimal price;
        private final Integer amount;

        public DeserializationImpl(final Long id, final String name, final BigDecimal price, final Integer amount) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.amount = amount;
        }

        @Override
        public Long getId() {
            return id;
        }

        @Override
        public String getName() {
            return name;
        }

        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public Integer getAmount() {
            return amount;
        }
    }
}
