package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeOnsiteQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
public class WaiterPlace implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final OrderService orderService;

    public WaiterPlace(WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, OrderService orderService) {
        this.waiterQueue = waiterQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        this.orderService = orderService;
        waiterQueue.registerObserver(this);
        ordersMadeOnsiteQueue.registerObserver(this);
    }

    @Override
    public void update(){
        startDelivering();
    }

    private void startDelivering() {
        if(isExistsWaiterAndOrder()){
            Employee employee = waiterQueue.get();
            OrderDTO order = ordersMadeOnsiteQueue.get();
            order.add(employee);
            startDeliveringOrder(employee);
            order.setHourAway(setHourAwayToOrder());
            orderService.update(order);
        }
    }

    private void startDeliveringOrder(Employee waiter) {
        Runnable r = () -> {
            preparing();
            waiterQueue.add(waiter);
        };
        new Thread(r).start();
    }

    private boolean isExistsWaiterAndOrder(){
        return ordersMadeOnsiteQueue.size()>0 && waiterQueue.size()>0;
    }

    private String setHourAwayToOrder(){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        return time.format(localDateTime);
    }

    private void preparing(){
        try {
            Thread.sleep(1000); // time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
