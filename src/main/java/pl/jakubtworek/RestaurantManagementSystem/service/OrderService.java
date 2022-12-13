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
    List<OrderDTO> findByParams(String date, String typeOfOrder, UUID employeeId, String username);
    List<OrderDTO> findMadeOrders();
    List<OrderDTO> findUnmadeOrders();
    void deleteByIdAndUsername(UUID theId, String username) throws OrderNotFoundException;
    List<OrderDTO> findAllByUsername(String username);
    Optional<OrderDTO> findByIdAndUsername(UUID theId, String username) throws OrderNotFoundException;
    List<OrderDTO> findMadeOrdersAndUsername(String username);
    List<OrderDTO> findUnmadeOrdersAndUsername(String username);
}
