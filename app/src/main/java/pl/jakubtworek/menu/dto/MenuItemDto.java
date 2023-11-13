package pl.jakubtworek.menu.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

@JsonDeserialize(as = MenuItemDto.DeserializationImpl.class)
public interface MenuItemDto {

    static MenuItemDto create(final Long id,
                              final String name,
                              final int price
    ) {
        return new MenuItemDto.DeserializationImpl(id, name, price);
    }

    Long getId();

    String getName();

    int getPrice();

    class DeserializationImpl implements MenuItemDto {
        private final Long id;
        private final String name;
        private final int price;

        DeserializationImpl(final Long id,
                            final String name,
                            final int price
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
        public int getPrice() {
            return price;
        }
    }
}
