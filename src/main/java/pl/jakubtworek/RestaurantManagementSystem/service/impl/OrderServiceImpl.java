package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrdersQueue ordersQueue;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrdersQueue ordersQueue) {
        this.orderRepository = orderRepository;
        this.ordersQueue = ordersQueue;
    }

    @Override
    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    @Override
    public Optional<Order> findById(Long theId) {
        return orderRepository.findById(theId);
    }

    @Override
    public Order save(OrderDTO orderDTO) {
        orderDTO.setId(0L);
        orderDTO.setPrice(countingOrderPrice(orderDTO));
        setDataForDTO(orderDTO);
        Order order = orderDTO.convertDTOToEntity();
        ordersQueue.add(order);
        order.setTypeOfOrder(orderDTO.getTypeOfOrder().convertDTOToEntity());
        order.getTypeOfOrder().add(order);
        orderRepository.save(order);
        return order;
    }
    @Override
    public void update(Order theOrder){
        orderRepository.save(theOrder);
    }

    @Override
    public void deleteById(Long theId) {
        orderRepository.deleteById(theId);
    }

    @Override
    public List<Order> findByDate(String theDate) {
        return orderRepository.findByDate(theDate);
    }

    @Override
    public List<Order> findByTypeOfOrder(TypeOfOrder typeOfOrder) {
        return orderRepository.findByTypeOfOrder(typeOfOrder);
    }

    @Override
    public List<Order> findByEmployee(Employee employee) {
        return orderRepository.findByEmployees(employee);
    }

    @Override
    public List<Order> findMadeOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.removeIf(order -> (order.getHourAway() == null));
        return orders;
    }

    @Override
    public List<Order> findUnmadeOrders() {
        List<Order> orders = orderRepository.findAll();
        orders.removeIf(order -> (order.getHourAway() != null));
        return orders;
    }

    @Override
    public boolean checkIfOrderIsNull(Long id){
        return findById(id).isEmpty();
    }

    private double countingOrderPrice(OrderDTO theOrder){
        double price = 0;
        for(MenuItemDTO tempMenuItem : theOrder.getMenuItems()){
            price += tempMenuItem.getPrice();
        }
        return price;
    }

    private void setDataForDTO(OrderDTO dto){
        LocalDateTime localDateTime = LocalDateTime.now();
        DateTimeFormatter date = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        DateTimeFormatter time = DateTimeFormatter.ofPattern("hh:mm");
        dto.setDate(date.format(localDateTime));
        dto.setHourOrder(time.format(localDateTime));
    }
}