package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.*;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueueFacade;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.*;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.*;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final TypeOfOrderRepository typeOfOrderRepository;
    private final UserRepository userRepository;
    private final MenuItemRepository menuItemRepository;
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
    public OrderDTO save(OrderRequest orderRequest) throws Exception {

        String typeOfOrderName = orderRequest.getTypeOfOrder();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MenuItemRequest> menuItemRequestList = orderRequest.getMenuItems();

        TypeOfOrderDTO typeOfOrderDTO = typeOfOrderRepository.findByType(typeOfOrderName)
                .orElseThrow(() -> new TypeOfOrderNotFoundException("Type of order not found in restaurant with that name: " + typeOfOrderName))
                .convertEntityToDTO();
        UserDTO userDTO = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for in restaurant with that username: " + username))
                .convertEntityToDTO();

        List<MenuItemDTO> menuItemDTOList = new ArrayList<>();
        for(MenuItemRequest miRequests : menuItemRequestList){
            String menuItemName = miRequests.getName();
            menuItemDTOList.add(menuItemRepository.findByName(menuItemName)
                    .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found in restaurant with that id: " + menuItemName))
                    .convertEntityToDTO());
        }

        Order order = orderFactory.createOrder(orderRequest, typeOfOrderDTO, menuItemDTOList, userDTO).convertDTOToEntity();
        OrderDTO orderCreated = orderRepository.save(order).convertEntityToDTO();
        ordersQueueFacade.addToQueue(orderCreated);
        return orderCreated;
    }

    @Override
    public void update(OrderDTO orderDTO){
        Order order = orderRepository.findById(orderDTO.getId()).orElse(orderDTO.convertDTOToEntity());
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
}