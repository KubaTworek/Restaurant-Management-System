package pl.jakubtworek.RestaurantManagementSystem.model.business;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.Observer;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersMadeDeliveryQueue;
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
public class DeliveryCar implements Observer {

    private final DeliveryQueue deliveryQueue;
    private final OrdersMadeDeliveryQueue ordersMadeDeliveryQueue;
    private final JdbcTemplate jdbc;
    private final OrderService orderService;

    public DeliveryCar(DeliveryQueue deliveryQueue, OrdersMadeDeliveryQueue ordersMadeDeliveryQueue, JdbcTemplate jdbc, OrderService orderService) {
        this.deliveryQueue = deliveryQueue;
        this.ordersMadeDeliveryQueue = ordersMadeDeliveryQueue;
        this.jdbc = jdbc;
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
            order.convertDTOToEntity().add(employee);
            orderService.update(order);
            startDeliveringOrder(employee,order,2);
        }
    }

    private void startDeliveringOrder(Employee delivery, OrderDTO order, int time) {
        Runnable r = () -> {
            preparing();
            deliveryQueue.add(delivery);
            setHourAwayToOrder(order);
            addMenuItemsToOrder(order);
        };
        new Thread(r).start();
    }

    private boolean isExistsDeliveryAndOrder(){
        return ordersMadeDeliveryQueue.size()>0 && deliveryQueue.size()>0;
    }

    private void setHourAwayToOrder(OrderDTO order){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm:ss");
        order.setHourAway(time.format(localDateTime));
        orderService.update(order);
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
            Thread.sleep(2000); // time
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
