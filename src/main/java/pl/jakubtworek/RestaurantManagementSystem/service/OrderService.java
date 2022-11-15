package pl.jakubtworek.RestaurantManagementSystem.service;

import org.springframework.data.jpa.repository.Query;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<Order> findAll();
    Optional<Order> findById(Long theId);
    Order save(OrderRequest theOrder);
    void deleteById(Long theId);
    void update(OrderDTO theOrder);
    List<Order> findByDate(String theDate);
    List<Order> findByEmployee(Employee employee);
    List<Order> findByTypeOfOrder(TypeOfOrder typeOfOrder);
    List<Order> findMadeOrders();
    List<Order> findUnmadeOrders();
    boolean checkIfOrderIsNull(Long id);
}
