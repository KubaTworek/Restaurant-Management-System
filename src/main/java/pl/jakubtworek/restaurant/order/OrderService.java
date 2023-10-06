package pl.jakubtworek.restaurant.order;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.auth.User;
import pl.jakubtworek.restaurant.auth.UserService;
import pl.jakubtworek.restaurant.business.queues.OrdersQueueFacade;
import pl.jakubtworek.restaurant.employee.Employee;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderService {
    private final UserService userService;
    private final OrderRepository orderRepository;
    private final OrdersQueueFacade ordersQueueFacade;

    OrderService(final UserService userService, final OrderRepository orderRepository, final OrdersQueueFacade ordersQueueFacade) {
        this.userService = userService;
        this.orderRepository = orderRepository;
        this.ordersQueueFacade = ordersQueueFacade;
    }

    OrderDto save(OrderRequest orderRequest, String jwt) {

        User user = userService.getUser(jwt);

        Order order = new Order();
        order.setPrice(calculatePrice(orderRequest.getMenuItems()));
        order.setHourOrder(ZonedDateTime.now());
        order.setTypeOfOrder(TypeOfOrder.valueOf(orderRequest.getTypeOfOrder()));
        order.setUser(user);

        OrderDto created = new OrderDto(orderRepository.save(order));
        ordersQueueFacade.addToQueue(created);
        return created;
    }

    public void update(OrderDto toUpdate) {

        Order order = new Order();
        order.setId(toUpdate.getId());
        order.setPrice(toUpdate.getPrice());
        order.setHourOrder(toUpdate.getHourOrder());
        order.setHourAway(toUpdate.getHourAway());
        order.setTypeOfOrder(toUpdate.getTypeOfOrder());
        order.setEmployees(toUpdate.getEmployees().stream().map(Employee::new).collect(Collectors.toList()));
        order.setUser(new User(toUpdate.getUserDto()));

        orderRepository.save(order);
    }

    List<OrderDto> findAll(String jwt) {
        String username = userService.getUser(jwt).getUsername();

        return orderRepository.findByUserUsername(username)
                .stream()
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    Optional<OrderDto> findById(Long id) {
        return orderRepository.findById(id)
                .map(OrderDto::new);
    }

    List<OrderDto> findByParams(String date, String typeOfOrder, Boolean isReady, Long employeeId, String username) {
        Stream<Order> orderStream = orderRepository.findAll().stream();

        if (date != null) {
            //orderStream = orderStream.filter(order -> date.equals(order.getDate()));
        }

        if (typeOfOrder != null) {
            orderStream = orderStream.filter(order -> TypeOfOrder.valueOf(typeOfOrder).equals(order.getTypeOfOrder()));
        }

        if (isReady != null) {
            //orderStream = orderStream.filter(order -> isReady == order.isReady());
        }

        if (employeeId != null) {
            orderStream = orderStream.filter(order -> order.getEmployees().stream().anyMatch(employee -> employeeId.equals(employee.getId())));
        }

        if (username != null) {
            orderStream = orderStream.filter(order -> username.equals(order.getUser().getUsername()));
        }

        return orderStream
                .map(OrderDto::new)
                .collect(Collectors.toList());
    }

    private int calculatePrice(final List<String> menuItems) {
        return 99;
    }
}