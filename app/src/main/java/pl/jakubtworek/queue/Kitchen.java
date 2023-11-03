package pl.jakubtworek.queue;

import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.OrderFacade;
import pl.jakubtworek.order.dto.SimpleOrder;

class Kitchen implements Observer {
    private final OrdersQueueFacade ordersQueueFacade;
    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;
    private final OrderFacade orderFacade;

    Kitchen(final OrdersQueueFacade ordersQueueFacade, final OrdersQueue ordersQueue, final CooksQueue cooksQueue, final OrderFacade orderFacade) {
        this.ordersQueueFacade = ordersQueueFacade;
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        this.orderFacade = orderFacade;
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
        final var cook = cooksQueue.get();
        final var order = ordersQueue.get();
        orderFacade.addEmployeeToOrder(order, cook);
        final int numberOfMenuItems = orderFacade.getNumberOfMenuItems(order);
        final int timeToCook = calculateCookingTime(numberOfMenuItems);
        startPreparingOrder(cook, order, timeToCook);
    }

    private int calculateCookingTime(int numberOfMenuItems) {
        return numberOfMenuItems * 1;
    } // 10 000

    private void startPreparingOrder(SimpleEmployee employee, SimpleOrder order, int time) {
        final var thread = new Thread(() -> {
            try {
                Thread.sleep(time);
                finishPreparingOrder(employee, order);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        thread.start();
    }

    private void finishPreparingOrder(SimpleEmployee employee, SimpleOrder order) {
        cooksQueue.add(employee);
        ordersQueueFacade.addReadyToQueue(order);
    }

    private boolean isExistsCookAndOrder() {
        return ordersQueue.size() > 0 && cooksQueue.size() > 0;
    }
}
