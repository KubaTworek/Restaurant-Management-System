package pl.jakubtworek.restaurant.order;

import pl.jakubtworek.restaurant.auth.UserDto;
import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.menu.MenuItemDto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDto {
    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private List<MenuItemDto> menuItems;
    private List<EmployeeDto> employees;
    private UserDto userDTO;

    OrderDto() {
    }

    public OrderDto(final Order source) {
        this.id = source.getId();
        this.price = source.getPrice();
        this.hourOrder = source.getHourOrder();
        this.hourAway = source.getHourAway();
        this.typeOfOrder = source.getTypeOfOrder();
        this.menuItems = source.getMenuItems().stream().map(MenuItemDto::new).collect(Collectors.toList());
        this.employees = source.getEmployees().stream().map(EmployeeDto::new).collect(Collectors.toList());
        this.userDTO = new UserDto(source.getUser());
    }

    Long getId() {
        return id;
    }

    public TypeOfOrder getTypeOfOrder() {
        return typeOfOrder;
    }

    public List<MenuItemDto> getMenuItems() {
        return menuItems;
    }

    int getPrice() {
        return price;
    }

    ZonedDateTime getHourOrder() {
        return hourOrder;
    }

    ZonedDateTime getHourAway() {
        return hourAway;
    }

    public void setHourAway(final ZonedDateTime hourAway) {
        this.hourAway = hourAway;
    }

    public void setUser(final UserDto userDTO) {
        this.userDTO = userDTO;
    }

    public List<EmployeeDto> getEmployees() {
        return employees;
    }

    UserDto getUserDto() {
        return userDTO;
    }

    public void add(EmployeeDto employee) {
        if (employee != null) {
            employees.add(employee);
            employee.getOrders().add(this);
        }
    }
}
