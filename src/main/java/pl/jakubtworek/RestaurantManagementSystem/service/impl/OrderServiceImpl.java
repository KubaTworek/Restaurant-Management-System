package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.*;
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
    public OrderDTO save(OrderRequest orderRequest) throws Exception {
        String typeOfOrderName = orderRequest.getTypeOfOrder();
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        List<MenuItemRequest> menuItemRequestList = orderRequest.getMenuItems();
        List<MenuItemDTO> menuItemDTOList = new ArrayList<>();

        TypeOfOrderDTO typeOfOrderDTO = getTypeOfOrderDTO(typeOfOrderName);
        UserDTO userDTO = getUserDTO(username);
        for(MenuItemRequest miRequests : menuItemRequestList){
            menuItemDTOList.add(getMenuItemDTO(miRequests.getName()));
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
    public void deleteById(Long theId) throws OrderNotFoundException {
        Order order = orderRepository.findById(theId)
                .orElseThrow(() -> new OrderNotFoundException("There are no order in restaurant with that id: " + theId));
        orderRepository.delete(order);
    }

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
    public List<OrderDTO> findByParams(String date, String typeOfOrder, Long employeeId) {
        TypeOfOrder typeOfOrderFound = typeOfOrderRepository.findByType(typeOfOrder).orElse(null);

        return getOrdersByParams(date, typeOfOrderFound, employeeId)
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findMadeOrders() {
        return orderRepository.findOrdersByHourAwayIsNotNull()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> findUnmadeOrders() {
        return orderRepository.findOrdersByHourAwayIsNull()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
    }

    private TypeOfOrderDTO getTypeOfOrderDTO(String typeOfOrderName) throws TypeOfOrderNotFoundException {
        return typeOfOrderRepository.findByType(typeOfOrderName)
                .orElseThrow(() -> new TypeOfOrderNotFoundException("Type of order not found in restaurant with that name: " + typeOfOrderName))
                .convertEntityToDTO();
    }

    private UserDTO getUserDTO(String username){
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found for in restaurant with that username: " + username))
                .convertEntityToDTO();
    }

    private MenuItemDTO getMenuItemDTO(String menuItemName) throws MenuItemNotFoundException {
        return menuItemRepository.findByName(menuItemName)
                .orElseThrow(() -> new MenuItemNotFoundException("Menu item not found in restaurant with that id: " + menuItemName))
                .convertEntityToDTO();
    }

    private List<Order> getOrdersByParams(String date, TypeOfOrder typeOfOrderFound, Long employeeId) {
        if(date == null && typeOfOrderFound == null && employeeId == null){
            return orderRepository.findAll();
        } else if (typeOfOrderFound == null && employeeId == null){
            return orderRepository.findByDate(date);
        } else if (date == null && employeeId == null){
            return orderRepository.findByTypeOfOrder(typeOfOrderFound);
        } else if (date == null && typeOfOrderFound == null){
            return orderRepository.findByEmployeesId(employeeId);
        } else if (employeeId == null){
            return orderRepository.findByDateAndTypeOfOrder(date, typeOfOrderFound);
        } else if (date == null){
            return orderRepository.findByTypeOfOrderAndEmployeesId(typeOfOrderFound, employeeId);
        } else if (typeOfOrderFound == null){
            return orderRepository.findByDateAndEmployeesId(date, employeeId);
        } else {
            return orderRepository.findByDateAndEmployeesIdAndTypeOfOrder(date, employeeId, typeOfOrderFound);
        }
    }
}