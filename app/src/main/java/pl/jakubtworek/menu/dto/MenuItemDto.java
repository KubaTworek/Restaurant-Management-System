package pl.jakubtworek.menu.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import pl.jakubtworek.order.dto.Status;

import java.math.BigDecimal;

@JsonDeserialize(as = MenuItemDto.DeserializationImpl.class)
public interface MenuItemDto {

    static MenuItemDto create(final Long id,
                              final String name,
                              final BigDecimal price,
                              final Status status
    ) {
        return new MenuItemDto.DeserializationImpl(id, name, price, status);
    }

    Long getId();

    String getName();

    BigDecimal getPrice();

    Status getStatus();

    class DeserializationImpl implements MenuItemDto {
        private final Long id;
        private final String name;
        private final BigDecimal price;
        private final Status status;

        DeserializationImpl(final Long id,
                            final String name,
                            final BigDecimal price,
                            final Status status
        ) {
            this.id = id;
            this.name = name;
            this.price = price;
            this.status = status;
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
        public Status getStatus() {
            return status;
        }
    }
}
