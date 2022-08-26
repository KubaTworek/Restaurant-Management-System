package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.dao.OrderDAO;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;

import java.util.List;

@Service
public class OrderServiceImpl implements OrderService{

    @Autowired
    private OrderDAO orderDAO;

    @Override
    public List<Order> findAll() {
        return null;
    }

    @Override
    public Order findById(int theId) {
        return null;
    }

    @Override
    public void save(Order theOrder) {

    }

    @Override
    public void deleteById(int theId) {

    }

    @Override
    public List<Order> findByDate(String theDate) {
        return null;
    }

    @Override
    public List<Order> findByEmployee(int employeeId) {
        return null;
    }

    @Override
    public List<Order> findByTypeOfOrder(String typeName) {
        return null;
    }
}
