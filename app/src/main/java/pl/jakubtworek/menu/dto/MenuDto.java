package pl.jakubtworek.menu.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

import java.util.List;

@JsonDeserialize(as = MenuDto.DeserializationImpl.class)
public interface MenuDto {

    static MenuDto create(final Long id, final String name, final List<MenuItemDto> menuItems) {
        return new DeserializationImpl(id, name, menuItems);
    }

    Long getId();

    String getName();

    List<MenuItemDto> getMenuItems();

    class DeserializationImpl implements MenuDto {
        private Long id;
        private String name;
        private List<MenuItemDto> menuItems;

        DeserializationImpl(final Long id, final String name, final List<MenuItemDto> menuItems) {
            this.id = id;
            this.name = name;
            this.menuItems = menuItems;
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
        public List<MenuItemDto> getMenuItems() {
            return menuItems;
        }
    }
}
