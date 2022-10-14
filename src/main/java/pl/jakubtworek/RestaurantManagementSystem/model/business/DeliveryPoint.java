package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class DeliveryPoint implements Observer{

    private DeliveryQueue deliveryQueue;
    private OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private OrderService orderService;

    public DeliveryPoint(DeliveryQueue deliveryQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue) {
        this.deliveryQueue = deliveryQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        deliveryQueue.registerObserver(this);
        ordersMadeDeliveryQueue.registerObserver(this);
    }

    public void startDelivering() {
        if(ordersMadeDeliveryQueue.size()>0 && deliveryQueue.size()>0){
            Employee employee = deliveryQueue.get();
            Order order = ordersMadeDeliveryQueue.get();
            int time = 2;
            jdbc.execute("INSERT INTO Order_Employee(id,order_id,employee_id) VALUES (0,"+order.getId()+","+employee.getId()+")");
            startDeliveringOrder(employee,order,time);
        }
    }

    @Override
    public void update(){
        startDelivering();
    }

    public void startDeliveringOrder(Employee delivery, Order order, int time) {
        Runnable r = new Runnable() {
            public void run() {
                for(int i = 0; i<time; i++){
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                deliveryQueue.add(delivery);

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
