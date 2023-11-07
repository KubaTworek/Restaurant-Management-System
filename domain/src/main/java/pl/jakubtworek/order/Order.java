package pl.jakubtworek.order;

import pl.jakubtworek.DomainEvent;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

class Order {
    static Order restore(OrderSnapshot snapshot) {
        return new Order(
                snapshot.getId(),
                snapshot.getPrice(),
                snapshot.getHourOrder(),
                snapshot.getHourAway(),
                snapshot.getTypeOfOrder(),
                snapshot.getMenuItems().stream().map(SimpleMenuItem::restore).collect(Collectors.toList()),
                snapshot.getEmployees().stream().map(SimpleEmployee::restore).collect(Collectors.toList()),
                SimpleUser.restore(snapshot.getUser())
        );
    }

    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private List<SimpleMenuItem> menuItems = new ArrayList<>();
    private List<SimpleEmployee> employees = new ArrayList<>();
    private SimpleUser user;

    public Order() {
    }

    private Order(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, final List<SimpleMenuItem> menuItems, final List<SimpleEmployee> employees, final SimpleUser user) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.menuItems = menuItems;
        this.employees = employees;
        this.user = user;
    }

    OrderSnapshot getSnapshot() {
        return new OrderSnapshot(
                id,
                price,
                hourOrder,
                hourAway,
                typeOfOrder,
                menuItems.stream().map(SimpleMenuItem::getSnapshot).collect(Collectors.toSet()),
                employees.stream().map(SimpleEmployee::getSnapshot).collect(Collectors.toSet()),
                user.getSnapshot()
        );
    }

    int getAmountOfMenuItems() {
        return menuItems.size();
    }

    void addEmployee(SimpleEmployee employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }

    void addMenuItem(SimpleMenuItem menuItem) {
        if (menuItem != null) {
            menuItems.add(menuItem);
        }
    }

    void delivery() {
        this.hourAway = ZonedDateTime.now();
    }

    void updateInfo(List<SimpleMenuItem> menuItems, String typeOfOrderName, SimpleUser user) {
        this.menuItems = menuItems;
        this.price = calculatePrice(menuItems);
        this.hourOrder = ZonedDateTime.now();
        this.typeOfOrder = TypeOfOrder.valueOf(typeOfOrderName);
        this.user = user;
    }

    private int calculatePrice(final List<SimpleMenuItem> menuItems) {
        return menuItems.stream()
                .mapToInt(SimpleMenuItem::getPrice)
                .sum();
    }

    DomainEvent sendToKitchen() {
        return new OrderEvent(
                this.id,
                typeOfOrder,
                this.menuItems.size(),
                OrderEvent.State.TODO
        );
    }
}