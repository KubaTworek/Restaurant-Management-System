package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

@Service
public class Kitchen implements Observer {

    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    @Autowired
    public Kitchen(OrdersQueue ordersQueue, CooksQueue cooksQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        ordersQueue.registerObserver(this);
        cooksQueue.registerObserver(this);
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
    }

    public void startCooking() {
        if(ordersQueue.size()>0 && cooksQueue.size()>0){
            Employee employee = cooksQueue.get();
            Order order = ordersQueue.get();
            int time = order.getMenuItems().size();
            order.add(employee);
            startDoingOrder(employee,order,time);
        }
    }

    @Override
    public void update(){
        startCooking();
    }

    public void startDoingOrder(Employee cook, Order order, int time) {
        Runnable r = () -> {
            for(int i = 0; i<time; i++){
                try {
                    Thread.sleep(1000); // czas
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            cooksQueue.add(cook);
            if(order.getTypeOfOrder().getId()==1)ordersMadeDeliveryQueue.add(order);
            if(order.getTypeOfOrder().getId()==2)ordersMadeOnsiteQueue.add(order);
        };
        new Thread(r).start();
    }
}
