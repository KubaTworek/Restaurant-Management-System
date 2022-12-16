package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;

@Service
public class Kitchen implements Observer {
    private final OrdersQueueFacade ordersQueueFacade;
    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;

    public Kitchen(OrdersQueueFacade ordersQueueFacade, OrdersQueue ordersQueue, CooksQueue cooksQueue) {
        this.ordersQueueFacade = ordersQueueFacade;
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        ordersQueue.registerObserver(this);
        cooksQueue.registerObserver(this);
    }

    @Override
    public void update() {
        if (isExistsCookAndOrder()) {
            startCooking();
        }
    }

    private void startCooking() {
        EmployeeDTO employee = cooksQueue.get();
        OrderDTO order = ordersQueue.get();
        order.add(employee);
        startPreparingOrder(employee, order, order.getMenuItems().size());
    }

    private void startPreparingOrder(EmployeeDTO employee, OrderDTO order, int time) {
        Runnable r = () -> preparing(employee, order, time);
        new Thread(r).start();
    }

    private boolean isExistsCookAndOrder() {
        return ordersQueue.size() > 0 && cooksQueue.size() > 0;
    }

    private void preparing(EmployeeDTO employee, OrderDTO order, int time) {
        int timeToCook = time * 1000;
        try {
            Thread.sleep(timeToCook);
            cooksQueue.add(employee);
            ordersQueueFacade.addMadeOrderToQueue(order);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
