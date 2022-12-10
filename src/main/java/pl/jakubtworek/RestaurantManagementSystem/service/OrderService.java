package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;

import java.util.*;

public interface OrderService {
    OrderDTO save(OrderRequest orderRequest) throws Exception;
    void deleteById(UUID theId) throws OrderNotFoundException;
    void update(OrderDTO theOrder);
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(UUID theId);
    List<OrderDTO> findByParams(String date, String typeOfOrder, UUID employeeId);
    List<OrderDTO> findMadeOrders();
    List<OrderDTO> findUnmadeOrders();
}
