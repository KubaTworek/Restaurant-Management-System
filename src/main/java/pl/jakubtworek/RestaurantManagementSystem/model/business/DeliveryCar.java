package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeDeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

@Service
public class DeliveryCar extends Delivery implements Observer {
    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    public DeliveryCar(OrderService orderService, DeliveryQueue deliveryQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        super(orderService);
        this.deliveryQueue = deliveryQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        deliveryQueue.registerObserver(this);
        ordersMadeDeliveryQueue.registerObserver(this);
    }

    @Override
    public void update(){
        if(isExistsEmployeeAndOrder()){
            startDelivering();
        }
    }

    @Override
    protected void startDelivering() {
        EmployeeDTO employee = deliveryQueue.get();
        OrderDTO order = ordersMadeDeliveryQueue.get();
        order.add(employee.convertDTOToEntity());
        startDeliveringOrder(2);
        order.setHourAway(setHourAwayToOrder());
        orderService.update(order);
        deliveryQueue.add(employee);
    }

    @Override
    protected boolean isExistsEmployeeAndOrder(){
        return ordersMadeDeliveryQueue.size()>0 && deliveryQueue.size()>0;
    }

    @Override
    protected void delivering(int time){
        int timeToDelivery = time * 10000;
        try {
            Thread.sleep(timeToDelivery);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
