package pl.jakubtworek.order;

import pl.jakubtworek.DomainEvent;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import java.time.ZonedDateTime;
import java.util.HashSet;
import java.util.Set;

class Order {
    private Long id;
    private int price;
    private ZonedDateTime hourOrder;
    private ZonedDateTime hourAway;
    private TypeOfOrder typeOfOrder;
    private Set<MenuItemId> menuItems = new HashSet<>();
    private Set<EmployeeId> employees = new HashSet<>();
    private UserId user;

    public Order() {
    }

    private Order(final Long id, final int price, final ZonedDateTime hourOrder, final ZonedDateTime hourAway, final TypeOfOrder typeOfOrder, final Set<MenuItemId> menuItems, final Set<EmployeeId> employees, final UserId user) {
        this.id = id;
        this.price = price;
        this.hourOrder = hourOrder;
        this.hourAway = hourAway;
        this.typeOfOrder = typeOfOrder;
        this.menuItems = menuItems;
        this.employees = employees;
        this.user = user;
    }

    static Order restore(OrderSnapshot snapshot) {
        return new Order(
                snapshot.getId(),
                snapshot.getPrice(),
                snapshot.getHourOrder(),
                snapshot.getHourAway(),
                snapshot.getTypeOfOrder(),
                snapshot.getMenuItems(),
                snapshot.getEmployees(),
                snapshot.getUser()
        );
    }

    OrderSnapshot getSnapshot() {
        return new OrderSnapshot(
                id,
                price,
                hourOrder,
                hourAway,
                typeOfOrder,
                menuItems,
                employees,
                user
        );
    }

    void addEmployee(EmployeeId employee) {
        if (employee != null) {
            employees.add(employee);
        }
    }

    void delivery() {
        this.hourAway = ZonedDateTime.now();
    }

    void updateInfo(Set<MenuItemId> menuItems, int price, String typeOfOrderName, UserId user) {
        this.menuItems = menuItems;
        this.price = price;
        this.hourOrder = ZonedDateTime.now();
        this.typeOfOrder = TypeOfOrder.valueOf(typeOfOrderName);
        this.user = user;
    }

    DomainEvent wasCookedBy(Cook cook) {
        return new OrderEvent(
                this.id,
                cook.getCookId(),
                typeOfOrder,
                this.menuItems.size(),
                OrderEvent.State.READY
        );
    }

    int calculateCookingTime() {
        return this.menuItems.size() * 1;
    } // 10 000
}