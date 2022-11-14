package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderFactory orderFactory;


    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderFactory orderFactory) {
        this.orderRepository = orderRepository;
        this.orderFactory = orderFactory;
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
    public Order save(OrderRequest orderDTO) {
        Order order = orderFactory.createOrder(orderDTO);
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




}