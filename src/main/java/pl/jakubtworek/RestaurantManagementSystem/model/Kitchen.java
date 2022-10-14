package pl.jakubtworek.RestaurantManagementSystem.model;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;

@Service
public class Kitchen implements Observer{

    private CooksQueue cooksQueue;
    private OrdersQueue ordersQueue;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private OrdersMadeOnsiteQueue ordersMadeOnsiteQueue;

    @Autowired
    private OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;

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
                if(order.getTypeOfOrder().getId()==1)ordersMadeDeliveryQueue.add(order);
                if(order.getTypeOfOrder().getId()==2)ordersMadeOnsiteQueue.add(order);
            }
        };
        new Thread(r).start();
    }
}
