package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.EmployeeDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.OrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.TypeOfOrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.Kitchen;
import pl.jakubtworek.RestaurantManagementSystem.model.OrdersQueue;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderDAO orderDAO;
    private final TypeOfOrderDAO typeOfOrderDAO;
    private final EmployeeDAO employeeDAO;

    @Autowired
    private OrdersQueue ordersQueue;

    @Autowired
    private JdbcTemplate jdbc;

    public OrderServiceImpl(OrderDAO orderDAO, TypeOfOrderDAO typeOfOrderDAO, EmployeeDAO employeeDAO) {
        this.orderDAO = orderDAO;
        this.typeOfOrderDAO = typeOfOrderDAO;
        this.employeeDAO = employeeDAO;
    }

    @Override
    public List<Order> findAll() {
        return orderDAO.findAll();
    }

    @Override
    public Order findById(int theId) {
        Optional<Order> result = orderDAO.findById(theId);

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

        orderDAO.save(theOrder);

        for(MenuItem menuItem : menuItems){
            jdbc.execute("INSERT INTO Order_Menu_Item(id,order_id,menu_item_id) VALUES (0,"+theOrder.getId()+","+menuItem.getId()+")");
        }

        theOrder.setMenuItems(menuItems);
        ordersQueue.add(theOrder);
    }
    @Override
    public void update(Order theOrder){

        orderDAO.save(theOrder);
    }

    @Override
    public void deleteById(int theId) {
        orderDAO.deleteById(theId);
    }

    @Override
    public List<Order> findByDate(String theDate) {
        return orderDAO.findByDate(theDate);
    }

    @Override
    public List<Order> findByTypeOfOrder(String typeName) {
        TypeOfOrder typeOfOrder = typeOfOrderDAO.findByType(typeName);
        return orderDAO.findByTypeOfOrder(typeOfOrder);
    }

    @Override
    public List<Order> findByEmployee(int employeeId) {
        List<Order> orders = orderDAO.findAll();
        orders.removeIf(order -> !(order.getEmployees().contains(employeeDAO.findById(employeeId))));
        return orders;
    }

    @Override
    public List<Order> findMadeOrders() {
        List<Order> orders = orderDAO.findAll();
        orders.removeIf(order -> (order.getHourAway() == null));
        return orders;
    }

    @Override
    public List<Order> findUnmadeOrders() {
        List<Order> orders = orderDAO.findAll();
        orders.removeIf(order -> (order.getHourAway() != null));
        return orders;
    }

    @Override
    public TypeOfOrder findTypeByName(String typeName){
        return typeOfOrderDAO.findByType(typeName);
    }

    public double countingOrderPrice(Order theOrder){
        double price = 0;
        for(MenuItem tempMenuItem : theOrder.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }
}
