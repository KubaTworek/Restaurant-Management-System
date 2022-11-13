package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.spy;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createEmployee;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuItemUtils.*;

public class OrderUtils {
    public static List<Order> createOrders(){
        Order order1 = new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteType(), List.of(createChickenMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get()));
        Order order2 = new Order(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryType(), List.of(createTiramisuMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get()));
        return List.of(order1, order2);
    }

    public static Optional<Order> createOnsiteOrder(){
        return Optional.of(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteType(), List.of(createChickenMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get())));
    }

    public static Optional<Order> createDeliveryOrder(){
        return Optional.of(new Order(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryType(), List.of(createTiramisuMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get())));
    }

    public static TypeOfOrder createOnsiteType(){
        return new TypeOfOrder(1L, "On-site", List.of());
    }

    public static TypeOfOrder createDeliveryType(){
        return new TypeOfOrder(2L, "Delivery", List.of());
    }
}
