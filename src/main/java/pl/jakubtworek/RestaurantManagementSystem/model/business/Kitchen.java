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

    @Override
    public void update(){
        startCooking();
    }

    private void startCooking() {
        if(isExistsCookAndOrder()){
            Employee employee = cooksQueue.get();
            Order order = ordersQueue.get();
            order.add(employee);
            startDoingOrder(employee,order,order.getMenuItems().size());
        }
    }

    private void startDoingOrder(Employee cook, Order order, int time) {
        Runnable r = () -> {
            preparing(time);
            cooksQueue.add(cook);
            serveOrder(order);
        };
        new Thread(r).start();
    }

    private boolean isExistsCookAndOrder(){
        return ordersQueue.size()>0 && cooksQueue.size()>0;
    }

    private void serveOrder(Order order){
        if(order.getTypeOfOrder().getId()==1) ordersMadeDeliveryQueue.add(order);
        if(order.getTypeOfOrder().getId()==2) ordersMadeOnsiteQueue.add(order);
    }

    private void preparing(int time){
        for(int i = 0; i<time; i++){
            try {
                Thread.sleep(1000); // czas
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
