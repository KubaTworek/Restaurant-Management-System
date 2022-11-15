package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.*;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;

@Service
public class Kitchen implements Observer {

    private final CooksQueue cooksQueue;
    private final OrdersQueue ordersQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    public Kitchen(OrdersQueue ordersQueue, CooksQueue cooksQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        ordersQueue.registerObserver(this);
        cooksQueue.registerObserver(this);
    }

    @Override
    public void update(){
        startCooking();
    }

    private void startCooking() {
        if(isExistsCookAndOrder()){
            EmployeeDTO employee = cooksQueue.get();
            OrderDTO order = ordersQueue.get();
            order.add(employee.convertDTOToEntity());
            startDoingOrder(employee,order,order.getMenuItems().size());
        }
    }

    private void startDoingOrder(EmployeeDTO cook, OrderDTO order, int time) {
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

    private void serveOrder(OrderDTO order){
        if(order.getTypeOfOrder().getId()==1) ordersMadeOnsiteQueue.add(order);
        if(order.getTypeOfOrder().getId()==2) ordersMadeDeliveryQueue.add(order);
    }

    private void preparing(int time){
        for(int i = 0; i<time; i++){
            try {
                Thread.sleep(1000); // time
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
