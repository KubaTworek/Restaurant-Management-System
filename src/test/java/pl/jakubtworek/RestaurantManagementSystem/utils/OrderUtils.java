package pl.jakubtworek.RestaurantManagementSystem.utils;

import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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

    public static class OrderAssertions<T extends Order>{

        public static void checkAssertionsForOrder(Order order){
            assertEquals(12.99, order.getPrice());
            assertEquals("2022-08-22", order.getDate());
            assertEquals("12:00", order.getHourOrder());
            assertEquals("12:15", order.getHourAway());
            assertEquals("On-site", order.getTypeOfOrder().getType());
            assertEquals("Chicken", order.getMenuItems().get(0).getName());
            assertEquals(10.99, order.getMenuItems().get(0).getPrice());
            assertEquals("Coke", order.getMenuItems().get(1).getName());
            assertEquals(1.99, order.getMenuItems().get(1).getPrice());
            assertEquals("John", order.getEmployees().get(0).getFirstName());
            assertEquals("Smith", order.getEmployees().get(0).getLastName());
            assertEquals("Cook", order.getEmployees().get(0).getJob().getName());
        }

        public static void checkAssertionsForOrders(List<Order> orders){
            assertEquals(12.99, orders.get(0).getPrice());
            assertEquals("2022-08-22", orders.get(0).getDate());
            assertEquals("12:00", orders.get(0).getHourOrder());
            assertEquals("12:15", orders.get(0).getHourAway());
            assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
            assertEquals("Chicken", orders.get(0).getMenuItems().get(0).getName());
            assertEquals(10.99, orders.get(0).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(0).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(0).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(0).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(0).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(0).getEmployees().get(0).getJob().getName());

            assertEquals(30.99, orders.get(1).getPrice());
            assertEquals("2022-08-22", orders.get(1).getDate());
            assertEquals("12:05", orders.get(1).getHourOrder());
            assertNull(orders.get(1).getHourAway());
            assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
            assertEquals("Tiramisu", orders.get(1).getMenuItems().get(0).getName());
            assertEquals(5.99, orders.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(1).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(1).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(1).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(1).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(1).getEmployees().get(0).getJob().getName());
        }
    }

    public static class OrderDTOAssertions<T extends OrderDTO>{

        public static void checkAssertionsForOrder(OrderDTO order){
            assertEquals(12.99, order.getPrice());
            assertEquals("2022-08-22", order.getDate());
            assertEquals("12:00", order.getHourOrder());
            assertEquals("12:15", order.getHourAway());
            assertEquals("On-site", order.getTypeOfOrder().getType());
            assertEquals("Chicken", order.getMenuItems().get(0).getName());
            assertEquals(10.99, order.getMenuItems().get(0).getPrice());
            assertEquals("Coke", order.getMenuItems().get(1).getName());
            assertEquals(1.99, order.getMenuItems().get(1).getPrice());
            assertEquals("John", order.getEmployees().get(0).getFirstName());
            assertEquals("Smith", order.getEmployees().get(0).getLastName());
            assertEquals("Cook", order.getEmployees().get(0).getJob().getName());
        }

        public static void checkAssertionsForOrders(List<OrderDTO> orders){
            assertEquals(12.99, orders.get(0).getPrice());
            assertEquals("2022-08-22", orders.get(0).getDate());
            assertEquals("12:00", orders.get(0).getHourOrder());
            assertEquals("12:15", orders.get(0).getHourAway());
            assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
            assertEquals("Chicken", orders.get(0).getMenuItems().get(0).getName());
            assertEquals(10.99, orders.get(0).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(0).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(0).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(0).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(0).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(0).getEmployees().get(0).getJob().getName());

            assertEquals(30.99, orders.get(1).getPrice());
            assertEquals("2022-08-22", orders.get(1).getDate());
            assertEquals("12:05", orders.get(1).getHourOrder());
            assertNull(orders.get(1).getHourAway());
            assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
            assertEquals("Tiramisu", orders.get(1).getMenuItems().get(0).getName());
            assertEquals(5.99, orders.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(1).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(1).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(1).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(1).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(1).getEmployees().get(0).getJob().getName());
        }
    }

    public static class OrderResponseAssertions<T extends OrderResponse>{

        public static void checkAssertionsForOrder(OrderResponse order){
            assertEquals(12.99, order.getPrice());
            assertEquals("2022-08-22", order.getDate());
            assertEquals("12:00", order.getHourOrder());
            assertEquals("12:15", order.getHourAway());
            assertEquals("On-site", order.getTypeOfOrder().getType());
            assertEquals("Chicken", order.getMenuItems().get(0).getName());
            assertEquals(10.99, order.getMenuItems().get(0).getPrice());
            assertEquals("Coke", order.getMenuItems().get(1).getName());
            assertEquals(1.99, order.getMenuItems().get(1).getPrice());
            assertEquals("John", order.getEmployees().get(0).getFirstName());
            assertEquals("Smith", order.getEmployees().get(0).getLastName());
            assertEquals("Cook", order.getEmployees().get(0).getJob().getName());
        }

        public static void checkAssertionsForOrders(List<OrderResponse> orders){
            assertEquals(12.99, orders.get(0).getPrice());
            assertEquals("2022-08-22", orders.get(0).getDate());
            assertEquals("12:00", orders.get(0).getHourOrder());
            assertEquals("12:15", orders.get(0).getHourAway());
            assertEquals("On-site", orders.get(0).getTypeOfOrder().getType());
            assertEquals("Chicken", orders.get(0).getMenuItems().get(0).getName());
            assertEquals(10.99, orders.get(0).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(0).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(0).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(0).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(0).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(0).getEmployees().get(0).getJob().getName());

            assertEquals(30.99, orders.get(1).getPrice());
            assertEquals("2022-08-22", orders.get(1).getDate());
            assertEquals("12:05", orders.get(1).getHourOrder());
            assertNull(orders.get(1).getHourAway());
            assertEquals("Delivery", orders.get(1).getTypeOfOrder().getType());
            assertEquals("Tiramisu", orders.get(1).getMenuItems().get(0).getName());
            assertEquals(5.99, orders.get(1).getMenuItems().get(0).getPrice());
            assertEquals("Coke", orders.get(1).getMenuItems().get(1).getName());
            assertEquals(1.99, orders.get(1).getMenuItems().get(1).getPrice());
            assertEquals("John", orders.get(1).getEmployees().get(0).getFirstName());
            assertEquals("Smith", orders.get(1).getEmployees().get(0).getLastName());
            assertEquals("Cook", orders.get(1).getEmployees().get(0).getJob().getName());
        }
    }
}
