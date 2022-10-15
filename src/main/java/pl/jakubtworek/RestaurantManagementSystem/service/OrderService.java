package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();
    Optional<Order> findById(int theId);
    Order save(Order theOrder);
    void deleteById(int theId);
    void update(Order theOrder);
    List<Order> findByDate(String theDate);
    List<Order> findByEmployee(int employeeId);
    List<Order> findByTypeOfOrder(String typeName);
    List<Order> findMadeOrders();
    List<Order> findUnmadeOrders();
    Optional<TypeOfOrder> findTypeByName(String typeName);
}
