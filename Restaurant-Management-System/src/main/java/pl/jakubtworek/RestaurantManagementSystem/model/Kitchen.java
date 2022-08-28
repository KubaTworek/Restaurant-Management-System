package pl.jakubtworek.RestaurantManagementSystem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.OrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@Service
public class Kitchen implements Observer{

    private CooksQueue cooksQueue;
    private OrdersQueue ordersQueue;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private OrderService orderService;

    public Kitchen(OrdersQueue ordersQueue, CooksQueue cooksQueue) {
        this.ordersQueue = ordersQueue;
        this.cooksQueue = cooksQueue;
        ordersQueue.registerObserver(this);
        cooksQueue.registerObserver(this);
    }

    public void startCooking() {
        if(ordersQueue.size()>0 && cooksQueue.size()>0){
            Employee employee = cooksQueue.get();
            Order order = ordersQueue.get();
            int time = order.getMenuItems().size();
            jdbc.execute("INSERT INTO Order_Employee(id,order_id,employee_id) VALUES (0,"+order.getId()+","+employee.getId()+")");
            startDoingOrder(employee,order,time);
        }
    }

    @Override
    public void update(){
        startCooking();
    }

    public void startDoingOrder(Employee cook, Order order, int time) {
        Runnable r = new Runnable() {
            public void run() {
                for(int i = 0; i<time; i++){
                    try {
                        Thread.sleep(30000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                cooksQueue.add(cook);
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
