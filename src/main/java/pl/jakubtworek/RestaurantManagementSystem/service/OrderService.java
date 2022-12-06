package pl.jakubtworek.RestaurantManagementSystem.service;

import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;

import java.util.*;

public interface OrderService {
    List<OrderDTO> findAll();
    Optional<OrderDTO> findById(Long theId);
    OrderDTO save(OrderRequest theOrder, TypeOfOrderDTO typeOfOrderDTO, List<MenuItemDTO> menuItemDTOList);
    void deleteById(Long theId);
    void update(OrderDTO theOrder);
    List<OrderDTO> findByDate(String theDate);
    List<OrderDTO> findByEmployeeId(Long employeeId);
    List<OrderDTO> findByTypeOfOrder(TypeOfOrder theTypeOfOrder);
    List<OrderDTO> findMadeOrders();
    List<OrderDTO> findUnmadeOrders();
}
