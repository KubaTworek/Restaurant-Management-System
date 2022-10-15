package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.business.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TypeOfOrderRepository typeOfOrderRepository;
    private final EmployeeRepository employeeRepository;
    private final OrdersQueue ordersQueue;
    private final JdbcTemplate jdbc;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, TypeOfOrderRepository typeOfOrderRepository, EmployeeRepository employeeRepository, OrdersQueue ordersQueue, JdbcTemplate jdbc) {
        this.orderRepository = orderRepository;
        this.typeOfOrderRepository = typeOfOrderRepository;
        this.employeeRepository = employeeRepository;
        this.ordersQueue = ordersQueue;
        this.jdbc = jdbc;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long theId) {
        return orderRepository.findById(theId);
    }

    @Override
    public Order save(Order theOrder) {
        theOrder.setPrice(countingOrderPrice(theOrder));
        Date date = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat timeFormat = new SimpleDateFormat("hh:ss");
        theOrder.setDate(dateFormat.format(date));
        theOrder.setHourOrder(timeFormat.format(date));

        List<MenuItem> menuItems = theOrder.getMenuItems();
        theOrder.setMenuItems(null);

        orderRepository.save(theOrder);

        for(MenuItem menuItem : menuItems){
            jdbc.execute("INSERT INTO order_menu_item(id,order_id,menu_item_id) VALUES (0,"+theOrder.getId()+","+menuItem.getId()+")");
        }

        theOrder.setMenuItems(menuItems);
        ordersQueue.add(theOrder);

        return theOrder;
    }
    @Override
    public void update(Order theOrder){
        orderRepository.save(theOrder);
    }

    @Override
    public void deleteById(Long theId) {
        orderRepository.deleteById(theId);
    }

    @Override
    public List<Order> findByDate(String theDate) {
        return orderRepository.findByDate(theDate);
    }

    @Override
    public List<Order> findByTypeOfOrder(String typeName) {
        if(typeOfOrderRepository.findByType(typeName).isPresent()){
            return orderRepository.findByTypeOfOrder(typeOfOrderRepository.findByType(typeName).get());
        }
        return Collections.emptyList();
    }

    @Override
    public List<Order> findByEmployee(Long employeeId) {
        List<Order> orders = orderRepository.findAll();
        if(employeeRepository.findById(employeeId).isPresent()){
            orders.removeIf(order -> !(order.getEmployees().contains(employeeRepository.findById(employeeId).get())));
            return orders;
        }
        return Collections.emptyList();
    }

    @Override
    public List<Order> findMadeOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.removeIf(order -> (order.getHourAway() == null));
        return orders;
    }

    @Override
    public List<Order> findUnmadeOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.removeIf(order -> (order.getHourAway() != null));
        return orders;
    }

    @Override
    public Optional<TypeOfOrder> findTypeByName(String typeName){
        return typeOfOrderRepository.findByType(typeName);
    }

    public double countingOrderPrice(Order theOrder){
        double price = 0;
        for(MenuItem tempMenuItem : theOrder.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }
}
