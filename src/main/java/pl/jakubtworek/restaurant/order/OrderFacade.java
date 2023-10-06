package pl.jakubtworek.restaurant.order;

import org.springframework.stereotype.Service;
import pl.jakubtworek.restaurant.auth.UserFacade;
import pl.jakubtworek.restaurant.auth.query.SimpleUserQueryDto;
import pl.jakubtworek.restaurant.business.queues.OrdersQueueFacade;
import pl.jakubtworek.restaurant.employee.EmployeeFacade;
import pl.jakubtworek.restaurant.employee.query.SimpleEmployeeQueryDto;
import pl.jakubtworek.restaurant.order.query.SimpleOrderQueryDto;
import pl.jakubtworek.restaurant.order.query.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class OrderFacade {
    private final UserFacade userFacade;
    private final EmployeeFacade employeeFacade;
    private final OrderRepository orderRepository;
    private final SimpleOrderQueryRepository simpleOrderQueryRepository;
    private final OrdersQueueFacade ordersQueueFacade;

    OrderFacade(final UserFacade userFacade, final EmployeeFacade employeeFacade, final OrderRepository orderRepository, final SimpleOrderQueryRepository simpleOrderQueryRepository, final OrdersQueueFacade ordersQueueFacade) {
        this.userFacade = userFacade;
        this.employeeFacade = employeeFacade;
        this.orderRepository = orderRepository;
        this.simpleOrderQueryRepository = simpleOrderQueryRepository;
        this.ordersQueueFacade = ordersQueueFacade;
    }

    public void update(SimpleOrderQueryDto toUpdate) {

        Order order = new Order();
        order.setId(toUpdate.getId());
        order.setPrice(toUpdate.getPrice());
        order.setHourOrder(toUpdate.getHourOrder());
        order.setHourAway(toUpdate.getHourAway());
        order.setTypeOfOrder(toUpdate.getTypeOfOrder());
        //order.setEmployees(); todo
        //order.setMenuItems(); todo
        //order.setUser(); todo

        orderRepository.save(order);
    }

    public void addEmployeeToOrder(SimpleOrderQueryDto order, SimpleEmployeeQueryDto employee) {
        Order orderEntity = orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
        SimpleEmployeeQueryDto employeeEntity = employeeFacade.getById(employee.getId());
        orderEntity.add(employeeEntity);
    }

    public SimpleOrderQueryDto getById(Long id) {
        return simpleOrderQueryRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    public int getNumberOfMenuItems(SimpleOrderQueryDto order) {
        return orderRepository.findById(order.getId())
                .map(o -> o.getMenuItems().size())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    OrderDto save(OrderRequest orderRequest, String jwt) {

        SimpleUserQueryDto user = userFacade.getUser(jwt);

        Order order = new Order();
        order.setPrice(calculatePrice(orderRequest.getMenuItems()));
        order.setHourOrder(ZonedDateTime.now());
        order.setTypeOfOrder(TypeOfOrder.valueOf(orderRequest.getTypeOfOrder()));
        order.setUser(user);

        OrderDto created = new OrderDto(orderRepository.save(order));
        SimpleOrderQueryDto orderQueryDto = SimpleOrderQueryDto.builder()
                .id(created.getId())
                .typeOfOrder(created.getTypeOfOrder())
                .hourOrder(created.getHourOrder())
                .price(created.getPrice())
                .build();
        ordersQueueFacade.addToQueue(orderQueryDto);
        return created;
    }

    List<OrderDto> findAll(String jwt) {
        SimpleUserQueryDto user = userFacade.getUser(jwt);

        return orderRepository.findByUserUsername(user.getUsername())
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
            //orderStream = orderStream.filter(order -> date.equals(order.getDate()));todo
        }

        if (typeOfOrder != null) {
            orderStream = orderStream.filter(order -> TypeOfOrder.valueOf(typeOfOrder).equals(order.getTypeOfOrder()));
        }

        if (isReady != null) {
            //orderStream = orderStream.filter(order -> isReady == order.isReady());todo
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
        return 99; //todo
    }
}