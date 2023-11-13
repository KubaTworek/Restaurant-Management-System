package pl.jakubtworek.menu.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.math.BigDecimal;

@JsonDeserialize(as = MenuItemDto.DeserializationImpl.class)
public interface MenuItemDto {

    static MenuItemDto create(final Long id,
                              final String name,
                              final BigDecimal price
    ) {
        return new MenuItemDto.DeserializationImpl(id, name, price);
    }

    Long getId();

    String getName();

    BigDecimal getPrice();

    class DeserializationImpl implements MenuItemDto {
        private final Long id;
        private final String name;
        private final BigDecimal price;

        DeserializationImpl(final Long id,
                            final String name,
                            final BigDecimal price
        ) {
            this.id = id;
            this.name = name;
            this.price = price;
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
    }
}
