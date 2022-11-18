package pl.jakubtworek.RestaurantManagementSystem.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.exception.JobNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final TypeOfOrderRepository typeOfOrderRepository;
    private final OrderFactory orderFactory;
    private final OrdersQueue ordersQueue;

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
        TypeOfOrder typeOfOrder = findTypeOfOrder(orderDTO.getTypeOfOrder());
        OrderDTO dto = createOrder(orderDTO, typeOfOrder);
        Order order = dto.convertDTOToEntity();
        Order orderCreated = orderRepository.save(order);
        ordersQueue.add(orderCreated.convertEntityToDTO());
        return orderCreated;
    }
    @Override
    public void update(OrderDTO theOrder){
        Order order = orderRepository.findById(theOrder.getId()).get();
        order.setHourAway(theOrder.getHourAway());
        order.setEmployees(theOrder.getEmployees());
        orderRepository.save(order);
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
        return orderRepository.findOrdersByHourAwayIsNotNull();
    }

    @Override
    public List<Order> findUnmadeOrders() {
        return orderRepository.findOrdersByHourAwayIsNull();
    }

    @Override
    public boolean checkIfOrderIsNull(Long id){
        return findById(id).isEmpty();
    }

    private TypeOfOrder findTypeOfOrder(String type){
        return typeOfOrderRepository.findByType(type).orElseThrow();
    }

    private OrderDTO createOrder(OrderRequest orderDTO, TypeOfOrder typeOfOrder){
        return orderFactory.createOrder(orderDTO, typeOfOrder).createOrder();
    }
}