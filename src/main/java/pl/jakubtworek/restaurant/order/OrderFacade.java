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

@Service
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
        Order orderEntity = orderQueryRepository.findById(order.getId())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
        SimpleEmployeeQueryDto employeeEntity = employeeFacade.getById(employee.getId());
        orderEntity.add(employeeEntity);
    }

    public SimpleOrderQueryDto getById(Long id) {
        return orderQueryRepository.findSimpleById(id)
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    public int getNumberOfMenuItems(SimpleOrderQueryDto order) {
        return orderQueryRepository.findById(order.getId())
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

        OrderDto created = orderRepository.saveAndReturnDto(order);
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

        return orderQueryRepository.findByUserUsername(user.getUsername());
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
    }

    List<OrderDto> findByParams(ZonedDateTime fromDate, ZonedDateTime toDate, String typeOfOrder, Boolean isReady, Long employeeId, String username) {
        return orderQueryRepository.findFilteredOrders(fromDate, toDate, typeOfOrder, isReady, employeeId, username);
    }

    private int calculatePrice(final List<String> menuItems) {
        return 99; //todo
    }
}