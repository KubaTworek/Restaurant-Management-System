package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;
    private final OrdersQueueFacade ordersQueueFacade;

    @Override
    public List<OrderDTO> findAll() {
        return orderRepository.findAll()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Optional<OrderDTO> findById(Long theId) {
        return orderRepository.findById(theId).map(Order::convertEntityToDTO);
    }

    @Override
    public OrderDTO save(OrderRequest orderRequest, TypeOfOrderDTO typeOfOrderDTO, List<MenuItemDTO> menuItemDTOList) {
        OrderDTO orderDTO = createOrder(orderRequest, typeOfOrderDTO, menuItemDTOList);
        Order order = orderDTO.convertDTOToEntity();
        Order orderCreated = orderRepository.save(order);
        if(orderCreated != null) {
            ordersQueueFacade.addToQueue(orderCreated.convertEntityToDTO());
            return orderCreated.convertEntityToDTO();
        }
        return order.convertEntityToDTO();
    }

    @Override
    public void update(OrderDTO orderDTO){
        Order order = orderRepository.findById(orderDTO.getId()).orElseThrow();
        order.setHourAway(orderDTO.getHourAway());
        order.setEmployees(orderDTO.getEmployees().stream().map(EmployeeDTO::convertDTOToEntity).collect(Collectors.toList()));
        orderRepository.save(order);
    }

    @Override
    public void deleteById(Long theId) {
        orderRepository.deleteById(theId);
    }

    @Override
    public List<OrderDTO> findByDate(String theDate) {
        return orderRepository.findByDate(theDate)
                .orElse(Collections.emptyList())
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findByTypeOfOrder(TypeOfOrder theTypeOfOrder) {
        return orderRepository.findByTypeOfOrder(theTypeOfOrder)
                .orElse(Collections.emptyList())
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findByEmployeeId(Long employeeId) {
        return orderRepository.findByEmployeesId(employeeId)
                .orElse(Collections.emptyList())
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findMadeOrders() {
        return orderRepository.findOrdersByHourAwayIsNotNull()
                .orElse(Collections.emptyList())
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findUnmadeOrders() {
        return orderRepository.findOrdersByHourAwayIsNull()
                .orElse(Collections.emptyList())
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private OrderDTO createOrder(OrderRequest orderDTO, TypeOfOrderDTO typeOfOrderDTO, List<MenuItemDTO> menuItemDTOList){
        return orderFactory.createOrder(orderDTO, typeOfOrderDTO, menuItemDTOList).createOrder();
    }
}