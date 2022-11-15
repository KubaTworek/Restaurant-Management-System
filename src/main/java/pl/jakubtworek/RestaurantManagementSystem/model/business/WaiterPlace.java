package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeOnsiteQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class WaiterPlace implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final JdbcTemplate jdbc;
    private final OrderService orderService;

    public WaiterPlace(WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, OrderService orderService, JdbcTemplate jdbc) {
        this.waiterQueue = waiterQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        waiterQueue.registerObserver(this);
        ordersMadeOnsiteQueue.registerObserver(this);
        this.orderService = orderService;
        this.jdbc = jdbc;
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
            startDeliveringOrder(employee,order);
            order.setHourAway(setHourAwayToOrder());
            orderService.update(order);
        }
    }

    private void startDeliveringOrder(Employee waiter, OrderDTO order) {
        Runnable r = () -> {
            preparing();
            waiterQueue.add(waiter);

            //addMenuItemsToOrder(order);
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

    private void addMenuItemsToOrder(OrderDTO order){
        List<MenuItem> menuItems = order.getMenuItems();
        order.setMenuItems(null);

        orderService.update(order);

        for(MenuItem menuItem : menuItems){
            jdbc.execute("INSERT INTO order_menu_item(id,order_id,menu_item_id) VALUES (0,"+order.getId()+","+menuItem.getId()+")");
        }
    }

    private void preparing(){
        try {
            Thread.sleep(1000); // time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
