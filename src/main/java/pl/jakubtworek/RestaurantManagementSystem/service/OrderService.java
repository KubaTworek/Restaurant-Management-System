package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.TypeOfOrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.List;
import java.util.Optional;

public interface OrderService {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Long theId);
    OrderDTO save(OrderRequest theOrder, TypeOfOrderDTO typeOfOrderDTO);
    void deleteById(Long theId);
    void update(OrderDTO theOrder);
    List<OrderDTO> findByDate(String theDate);
    List<OrderDTO> findByEmployee(Employee theEmployee);
    List<OrderDTO> findByTypeOfOrder(TypeOfOrder theTypeOfOrder);
    List<OrderDTO> findMadeOrders();
    List<OrderDTO> findUnmadeOrders();
}
