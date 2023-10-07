package pl.jakubtworek.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.business.queues.CooksQueue;
import pl.jakubtworek.business.queues.Observer;
import pl.jakubtworek.business.queues.OrdersQueue;
import pl.jakubtworek.business.queues.OrdersQueueFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployeeQueryDto;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrderQueryDto;

@Service
class Kitchen implements Observer {
    private final OrdersQueueFacade ordersQueueFacade;
    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;
    private final OrderFacade orderFacade;
    private final EmployeeFacade employeeFacade;

    Kitchen(final OrdersQueueFacade ordersQueueFacade, final OrdersQueue ordersQueue, final CooksQueue cooksQueue, final OrderFacade orderFacade, final EmployeeFacade employeeFacade) {
        this.ordersQueueFacade = ordersQueueFacade;
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        this.orderFacade = orderFacade;
        this.employeeFacade = employeeFacade;
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
        SimpleEmployeeQueryDto employee = cooksQueue.get();
        SimpleOrderQueryDto order = ordersQueue.get();
        orderFacade.addEmployeeToOrder(order, employee);
        employeeFacade.addOrderToEmployee(employee, order);
        int numberOfMenuItems = orderFacade.getNumberOfMenuItems(order);
        startPreparingOrder(employee, order, numberOfMenuItems);
    }

    private void startPreparingOrder(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time) {
        Runnable r = () -> preparing(employee, order, time);
        new Thread(r).start();
    }

    private boolean isExistsCookAndOrder() {
        return ordersQueue.size() > 0 && cooksQueue.size() > 0;
    }

    private void preparing(SimpleEmployeeQueryDto employee, SimpleOrderQueryDto order, int time) {
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
