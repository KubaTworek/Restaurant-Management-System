package pl.jakubtworek.RestaurantManagementSystem.model.business;

import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public abstract class Delivery {
    final OrderService orderService;

    Delivery(OrderService orderService) {
        this.orderService = orderService;
    }

    abstract void startDelivering();

    abstract boolean isExistsEmployeeAndOrder();

    abstract void delivering(EmployeeDTO employee, OrderDTO order, int time);

    void startDeliveringOrder(EmployeeDTO employee, OrderDTO order, int time) {
        Runnable r = () -> delivering(employee, order, time);
        new Thread(r).start();
    }

    String setHourAwayToOrder() {
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }
}
