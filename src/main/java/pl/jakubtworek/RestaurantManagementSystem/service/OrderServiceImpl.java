package pl.jakubtworek.RestaurantManagementSystem.service;

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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderRepository orderRepository;
    private final TypeOfOrderRepository typeOfOrderRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    private OrdersQueue ordersQueue;

    @Autowired
    private JdbcTemplate jdbc;

    public OrderServiceImpl(OrderRepository orderRepository, TypeOfOrderRepository typeOfOrderRepository, EmployeeRepository employeeRepository) {
        this.orderRepository = orderRepository;
        this.typeOfOrderRepository = typeOfOrderRepository;
        this.employeeRepository = employeeRepository;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Order findById(int theId) {
        Optional<Order> result = orderRepository.findById(theId);

        Order theOrder = null;

        if (result.isPresent()) {
            theOrder = result.get();
        }

        return theOrder;
    }

    @Override
    public void save(Order theOrder) {
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
            jdbc.execute("INSERT INTO Order_Menu_Item(id,order_id,menu_item_id) VALUES (0,"+theOrder.getId()+","+menuItem.getId()+")");
        }

        theOrder.setMenuItems(menuItems);
        ordersQueue.add(theOrder);
    }
    @Override
    public void update(Order theOrder){

        orderRepository.save(theOrder);
    }

    @Override
    public void deleteById(int theId) {
        orderRepository.deleteById(theId);
    }

    @Override
    public List<Order> findByDate(String theDate) {
        return orderRepository.findByDate(theDate);
    }

    @Override
    public List<Order> findByTypeOfOrder(String typeName) {
        TypeOfOrder typeOfOrder = typeOfOrderRepository.findByType(typeName);
        return orderRepository.findByTypeOfOrder(typeOfOrder);
    }

    @Override
    public List<Order> findByEmployee(int employeeId) {
        List<Order> orders = orderRepository.findAll();
        orders.removeIf(order -> !(order.getEmployees().contains(employeeRepository.findById(employeeId))));
        return orders;
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
    public TypeOfOrder findTypeByName(String typeName){
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
