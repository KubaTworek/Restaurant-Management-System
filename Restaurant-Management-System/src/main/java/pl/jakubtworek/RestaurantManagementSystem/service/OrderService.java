package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.entity.TypeOfOrder;

import java.util.List;

public interface OrderService {
    List<Order> findAll();
    Order findById(int theId);
    void save(Order theOrder);
    void deleteById(int theId);

    List<Order> findByDate(String theDate);
    List<Order> findByEmployee(int employeeId);
    List<Order> findByTypeOfOrder(String typeName);
    List<Order> findMadeOrders();
    List<Order> findUnmadeOrders();
    TypeOfOrder findTypeByName(String typeName);
}
