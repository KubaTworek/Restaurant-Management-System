package pl.jakubtworek.restaurant.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.business.queues.CooksQueue;
import pl.jakubtworek.restaurant.business.queues.Observer;
import pl.jakubtworek.restaurant.business.queues.OrdersQueue;
import pl.jakubtworek.restaurant.business.queues.OrdersQueueFacade;
import pl.jakubtworek.restaurant.employee.EmployeeDto;
import pl.jakubtworek.restaurant.order.OrderDto;

@Service
class Kitchen implements Observer {
    private final OrdersQueueFacade ordersQueueFacade;
    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;

    Kitchen(OrdersQueueFacade ordersQueueFacade, OrdersQueue ordersQueue, CooksQueue cooksQueue) {
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
        EmployeeDto employee = cooksQueue.get();
        OrderDto order = ordersQueue.get();
        order.add(employee);
        startPreparingOrder(employee, order, order.getMenuItems().size());
    }

    private void startPreparingOrder(EmployeeDto employee, OrderDto order, int time) {
        Runnable r = () -> preparing(employee, order, time);
        new Thread(r).start();
    }

    private boolean isExistsCookAndOrder() {
        return ordersQueue.size() > 0 && cooksQueue.size() > 0;
    }

    private void preparing(EmployeeDto employee, OrderDto order, int time) {
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
