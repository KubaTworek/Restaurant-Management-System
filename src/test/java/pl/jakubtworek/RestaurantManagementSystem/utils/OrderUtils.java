package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.spy;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createEmployee;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createEmployeeDTO;
import static pl.jakubtworek.RestaurantManagementSystem.utils.MenuItemUtils.*;

public class OrderUtils {
    public static List<Order> createOrders(){
        Order order1 = new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteType(), List.of(createChickenMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get()));
        Order order2 = new Order(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryType(), List.of(createTiramisuMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get()));
        return List.of(order1, order2);
    }

    public static List<OrderDTO> createOrdersDTO(){
        OrderDTO order1 = new OrderDTO(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteTypeDTO(), List.of(createChickenMenuItemDTO().get(), createCokeMenuItemDTO().get()), List.of(createEmployeeDTO().get()));
        OrderDTO order2 = new OrderDTO(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryTypeDTO(), List.of(createTiramisuMenuItemDTO().get(), createCokeMenuItemDTO().get()), List.of(createEmployeeDTO().get()));
        return List.of(order1, order2);
    }

    public static OrderRequest createOnsiteOrderRequest(){
        return new OrderRequest("On-site", List.of(new MenuItemRequest("Chicken", 10.99, "Food"), new MenuItemRequest("Coke", 1.99, "Drinks")));
    }

    public static Optional<Order> createOnsiteOrder(){
        return Optional.of(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteType(), List.of(createChickenMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get())));
    }

    public static Optional<OrderDTO> createOnsiteOrderDTO(){
        return Optional.of(new OrderDTO(1L, 12.99, "2022-08-22", "12:00", "12:15", createOnsiteTypeDTO(), List.of(createChickenMenuItemDTO().get(), createCokeMenuItemDTO().get()), List.of(createEmployeeDTO().get())));
    }

    public static Optional<Order> createDeliveryOrder(){
        return Optional.of(new Order(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryType(), List.of(createTiramisuMenuItem().get(), createCokeMenuItem().get()), List.of(createEmployee().get())));
    }

    public static Optional<OrderDTO> createDeliveryOrderDTO(){
        return Optional.of(new OrderDTO(2L, 30.99, "2022-08-22", "12:05", null, createDeliveryTypeDTO(), List.of(createTiramisuMenuItemDTO().get(), createCokeMenuItemDTO().get()), List.of(createEmployeeDTO().get())));
    }

    public static TypeOfOrder createOnsiteType(){
        return new TypeOfOrder(1L, "On-site", List.of());
    }

    public static TypeOfOrderDTO createOnsiteTypeDTO(){
        return new TypeOfOrderDTO(1L, "On-site", List.of());
    }


    public static TypeOfOrder createDeliveryType(){
        return new TypeOfOrder(2L, "Delivery", List.of());
    }

    public static TypeOfOrderDTO createDeliveryTypeDTO(){
        return new TypeOfOrderDTO(2L, "Delivery", List.of());
    }
}
