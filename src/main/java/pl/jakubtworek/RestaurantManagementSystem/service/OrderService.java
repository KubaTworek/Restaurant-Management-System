package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import java.util.*;

public interface OrderService {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Long theId);
    OrderDTO save(OrderRequest orderRequest) throws Exception;
    void deleteById(Long theId) throws OrderNotFoundException;
    void update(OrderDTO theOrder);
    List<OrderDTO> findByParams(String date, String typeOfOrder, Long employeeId);
    List<OrderDTO> findMadeOrders();
    List<OrderDTO> findUnmadeOrders();
}
