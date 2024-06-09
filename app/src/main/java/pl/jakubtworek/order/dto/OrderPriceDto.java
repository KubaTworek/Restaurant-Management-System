package pl.jakubtworek.order.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(as = OrderPriceDto.DeserializationImpl.class)
public interface OrderPriceDto {
    static OrderPriceDto create(final Long id,
                                final BigDecimal price,
                                final BigDecimal deliveryFee,
                                final BigDecimal minimumBasketFee,
                                final BigDecimal tip
    ) {
        return new OrderPriceDto.DeserializationImpl(id, price, deliveryFee, minimumBasketFee, tip);
    }

    Long getId();

    BigDecimal getPrice();

    BigDecimal getDeliveryFee();

    BigDecimal getMinimumBasketFee();

    BigDecimal getTip();

    class DeserializationImpl implements OrderPriceDto {
        private final Long id;
        private final BigDecimal price;
        private final BigDecimal deliveryFee;
        private final BigDecimal minimumBasketFee;
        private final BigDecimal tip;

        DeserializationImpl(final Long id,
                            final BigDecimal price,
                            final BigDecimal deliveryFee,
                            final BigDecimal minimumBasketFee,
                            final BigDecimal tip
        ) {
            this.id = id;
            this.price = price;
            this.deliveryFee = deliveryFee;
            this.minimumBasketFee = minimumBasketFee;
            this.tip = tip;
        }

        @Override
        public Long getId() {
            return id;
        }
        @Override
        public BigDecimal getPrice() {
            return price;
        }

        @Override
        public BigDecimal getDeliveryFee() {
            return deliveryFee;
        }

        @Override
        public BigDecimal getMinimumBasketFee() {
            return minimumBasketFee;
        }

        @Override
        public BigDecimal getTip() {
            return tip;
        }
    }
}
