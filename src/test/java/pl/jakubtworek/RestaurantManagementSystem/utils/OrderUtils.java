package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createCook;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuUtils.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.UserUtils.createUser;

public class OrderUtils {
    public static List<Order> createOrders(){
        Order order1 = createOnsiteOrder();
        Order order2 = createDeliveryOrder();
        return List.of(order1, order2);
    }

    public static OrderRequest createOnsiteOrderRequest(){
        return new OrderRequest("On-site", List.of(createChickenMenuItemRequest(), new MenuItemRequest("Coke", 1.99, "Drinks")));
    }

    public static Order createOnsiteOrder(){
        return new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteType(), List.of(createChickenMenuItem(), createCokeMenuItem()), List.of(createCook()), createUser());
    }

    public static Order createDeliveryOrder(){
        return new Order(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryType(), List.of(createTiramisuMenuItem(), createCokeMenuItem()), List.of(createCook()), createUser());
    }

    public static TypeOfOrder createOnsiteType(){
        return new TypeOfOrder(1L, "On-site", List.of());
    }

    public static TypeOfOrder createDeliveryType(){
        return new TypeOfOrder(2L, "Delivery", List.of());
    }
}
