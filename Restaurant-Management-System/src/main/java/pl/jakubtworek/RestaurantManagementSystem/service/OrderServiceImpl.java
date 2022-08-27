package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.EmployeeDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.OrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.dao.TypeOfOrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService{

    private final OrderDAO orderDAO;
    private final TypeOfOrderDAO typeOfOrderDAO;
    private final EmployeeDAO employeeDAO;

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
}
