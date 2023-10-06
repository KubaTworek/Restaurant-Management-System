package pl.jakubtworek.restaurant.business;

import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.order.OrderDto;
import pl.jakubtworek.restaurant.order.OrderService;

abstract class Delivery {
    final OrderService orderService;

    Delivery(OrderService orderService) {
        this.orderService = orderService;
    }

    abstract void startDelivering();

    abstract boolean isExistsEmployeeAndOrder();

    abstract void delivering(EmployeeDto employee, OrderDto order, int time);

    void startDeliveringOrder(EmployeeDto employee, OrderDto order, int time) {
        Runnable r = () -> delivering(employee, order, time);
        new Thread(r).start();
    }
}
