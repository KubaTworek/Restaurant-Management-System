package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeDeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class DeliveryCar implements Observer {

    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;
    private final OrderService orderService;

    public DeliveryCar(DeliveryQueue deliveryQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue, OrderService orderService) {
        this.deliveryQueue = deliveryQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        this.orderService = orderService;
        deliveryQueue.registerObserver(this);
        ordersMadeDeliveryQueue.registerObserver(this);
    }

    @Override
    public void update(){
        startDelivering();
    }

    private void startDelivering() {
        if(isExistsDeliveryAndOrder()){
            Employee employee = deliveryQueue.get();
            OrderDTO order = ordersMadeDeliveryQueue.get();
            order.add(employee);
            startDeliveringOrder(employee,2);
            order.setHourAway(setHourAwayToOrder());
            orderService.update(order);
        }
    }

    private void startDeliveringOrder(Employee delivery, int time) {
        Runnable r = () -> {
            preparing();
            deliveryQueue.add(delivery);
        };
        new Thread(r).start();
    }

    private boolean isExistsDeliveryAndOrder(){
        return ordersMadeDeliveryQueue.size()>0 && deliveryQueue.size()>0;
    }

    private String setHourAwayToOrder(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }

    private void preparing(){
        try {
            Thread.sleep(2000); // time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
