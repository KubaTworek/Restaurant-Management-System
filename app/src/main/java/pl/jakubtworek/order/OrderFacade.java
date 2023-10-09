package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.SimpleUser;
import pl.jakubtworek.queue.OrdersQueueFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class OrderFacade {
    private final UserFacade userFacade;
    private final EmployeeFacade employeeFacade;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrdersQueueFacade ordersQueueFacade;

    OrderFacade(final UserFacade userFacade, final EmployeeFacade employeeFacade, final OrderRepository orderRepository, final OrderQueryRepository orderQueryRepository, final OrdersQueueFacade ordersQueueFacade) {
        this.userFacade = userFacade;
        this.employeeFacade = employeeFacade;
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.ordersQueueFacade = ordersQueueFacade;
    }

    public void update(SimpleOrder toUpdate) {

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

    public void addEmployeeToOrder(SimpleOrder order, SimpleEmployee employee) {
        Order orderEntity = orderRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
        SimpleEmployee employeeEntity = employeeFacade.getById(employee.getId());
        orderEntity.add(employeeEntity);
    }

    public SimpleOrder getById(Long id) {
        return orderQueryRepository.findSimpleById(id)
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    public int getNumberOfMenuItems(SimpleOrder order) {
        return orderRepository.findById(order.getId())
                .map(o -> o.getMenuItems().size())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    OrderDto save(OrderRequest orderRequest, String jwt) {

        SimpleUser user = userFacade.getUser(jwt);

        Order order = new Order();
        order.setPrice(calculatePrice(orderRequest.getMenuItems()));
        order.setHourOrder(ZonedDateTime.now());
        order.setTypeOfOrder(TypeOfOrder.valueOf(orderRequest.getTypeOfOrder()));
        order.setUser(user);

        OrderDto created = toDto(orderRepository.save(order));
        SimpleOrder orderQueryDto = new SimpleOrder(
                created.getId(),
                created.getPrice(),
                created.getHourOrder(),
                null,
                created.getTypeOfOrder()

        );
        ordersQueueFacade.addToQueue(orderQueryDto);
        return created;
    }

    List<OrderDto> findAll(String jwt) {
        SimpleUser user = userFacade.getUser(jwt);

        return orderQueryRepository.findByUserUsername(user.getUsername());
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
    }

    List<OrderDto> findByParams(ZonedDateTime fromDate, ZonedDateTime toDate, String typeOfOrder, Boolean isReady, Long employeeId, String username) {
        return orderQueryRepository.findFilteredOrders(fromDate, toDate, typeOfOrder, isReady, employeeId, username);
    }

    private int calculatePrice(List<String> menuItems) {
        return 99; //todo
    }

    private OrderDto toDto(Order order) {
        return OrderDto.builder()
                .withId(order.getId())
                .withPrice(order.getPrice())
                .withHourOrder(order.getHourOrder())
                .withHourAway(order.getHourAway())
                .withTypeOfOrder(order.getTypeOfOrder())
                .build();
    }
}