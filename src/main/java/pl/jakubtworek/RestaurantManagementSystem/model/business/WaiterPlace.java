package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeOnsiteQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WaiterPlace implements Observer {

    private final WaiterQueue waiterQueue;
    private final OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;
    private final JdbcTemplate jdbc;
    private final OrderService orderService;


    @Autowired
    public WaiterPlace(WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue, OrderService orderService, JdbcTemplate jdbc) {
        this.waiterQueue = waiterQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        waiterQueue.registerObserver(this);
        ordersMadeOnsiteQueue.registerObserver(this);
        this.orderService = orderService;
        this.jdbc = jdbc;
    }

    public void startDelivering() {
        if(ordersMadeOnsiteQueue.size()>0 && waiterQueue.size()>0){
            Employee employee = waiterQueue.get();
            Order order = ordersMadeOnsiteQueue.get();
            int time = 1;
            order.add(employee);
            startDeliveringOrder(employee,order,time);
        }
    }

    @Override
    public void update(){
        startDelivering();
    }

    public void startDeliveringOrder(Employee waiter, Order order, int time) {
        Runnable r = () -> {
            for(int i = 0; i<time; i++){
                try {
                    Thread.sleep(1000); //czas
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            waiterQueue.add(waiter);

            Date date = new Date();
            SimpleDateFormat timeFormat = new SimpleDateFormat("hh:ss");
            order.setHourAway(timeFormat.format(date));

            List<MenuItem> menuItems = order.getMenuItems();
            order.setMenuItems(null);

            orderService.update(order);

            for(MenuItem menuItem : menuItems){
                jdbc.execute("INSERT INTO order_menu_item(id,order_id,menu_item_id) VALUES (0,"+order.getId()+","+menuItem.getId()+")");
            }
        };
        new Thread(r).start();
    }
}
