package pl.jakubtworek.order.dto;

import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.menu.dto.MenuItemDto;

import java.time.ZonedDateTime;
import java.util.Set;

public class OrderResponse {
    private final Long id;
    private final int price;
    private final ZonedDateTime hourOrder;
    private final ZonedDateTime hourAway;
    private final TypeOfOrder typeOfOrder;
    private final Set<MenuItemDto> menuItems;
    private final Set<EmployeeDto> employees;

    public OrderResponse(final Long id,
                         final int price,
                         final ZonedDateTime hourOrder,
                         final ZonedDateTime hourAway,
                         final TypeOfOrder typeOfOrder,
                         final Set<MenuItemDto> menuItems,
                         final Set<EmployeeDto> employees) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.menuItems = menuItems;
        this.employees = employees;
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

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    public Set<MenuItemDto> getMenuItems() {
        return menuItems;
    }

    public Set<EmployeeDto> getEmployees() {
        return employees;
    }
}
