package pl.jakubtworek.RestaurantManagementSystem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class WaiterPoint implements Observer{

    private WaiterQueue waiterQueue;
    private OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private OrderService orderService;


    public WaiterPoint(WaiterQueue waiterQueue, OrdersMadeOnsiteQueue ordersMadeOnsiteQueue) {
        this.waiterQueue = waiterQueue;
        this.ordersMadeOnsiteQueue = ordersMadeOnsiteQueue;
        waiterQueue.registerObserver(this);
        ordersMadeOnsiteQueue.registerObserver(this);
    }

    public void startDelivering() {
        if(ordersMadeOnsiteQueue.size()>0 && waiterQueue.size()>0){
            Employee employee = waiterQueue.get();
            Order order = ordersMadeOnsiteQueue.get();
            int time = 1;
            jdbc.execute("INSERT INTO Order_Employee(id,order_id,employee_id) VALUES (0,"+order.getId()+","+employee.getId()+")");
            startDeliveringOrder(employee,order,time);
        }
    }

    @Override
    public void update(){
        startDelivering();
    }

    public void startDeliveringOrder(Employee waiter, Order order, int time) {
        Runnable r = new Runnable() {
            public void run() {
                for(int i = 0; i<time; i++){
                    try {
                        Thread.sleep(30000);
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
                    jdbc.execute("INSERT INTO Order_Menu_Item(id,order_id,menu_item_id) VALUES (0,"+order.getId()+","+menuItem.getId()+")");
                }
            }
        };
        new Thread(r).start();
    }
}
