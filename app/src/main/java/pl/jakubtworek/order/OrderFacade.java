package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.SimpleEmployee;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.SimpleOrder;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.queue.OrdersQueueFacade;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class OrderFacade {
    private final UserFacade userFacade;
    private final EmployeeFacade employeeFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrdersQueueFacade ordersQueueFacade;

    OrderFacade(final UserFacade userFacade, final EmployeeFacade employeeFacade, final MenuItemFacade menuItemFacade,
                final OrderRepository orderRepository, final OrderQueryRepository orderQueryRepository,
                final OrdersQueueFacade ordersQueueFacade) {
        this.userFacade = userFacade;
        this.employeeFacade = employeeFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.ordersQueueFacade = ordersQueueFacade;
    }

    public void setAsDelivered(SimpleOrder toUpdate) {
        orderRepository.findById(toUpdate.getId())
                .map(o -> {
                    o.setHourAway(ZonedDateTime.now());
                    return orderRepository.save(o);
                })
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    public void addEmployeeToOrder(SimpleOrder order, SimpleEmployee employee) {
        orderRepository.findById(order.getId())
                .map(o -> {
                    final var employeeEntity = employeeFacade.getById(employee.getId());
                    o.add(employeeEntity);
                    return orderRepository.save(o);
                })
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    public int getNumberOfMenuItems(SimpleOrder order) {
        return orderRepository.findById(order.getId())
                .map(o -> o.getMenuItems().size())
                .orElseThrow(() -> new IllegalStateException("Order with that id doesn't exist"));
    }

    OrderDto save(OrderRequest orderRequest, String jwt) {

        final var user = userFacade.getUser(jwt);

        final var order = new Order();
        order.setPrice(calculatePrice(orderRequest.getMenuItems()));
        order.setHourOrder(ZonedDateTime.now());
        order.setTypeOfOrder(TypeOfOrder.valueOf(orderRequest.getTypeOfOrder()));
        order.setUser(user);

        final var created = toDto(orderRepository.save(order));
        final var orderQueryDto = new SimpleOrder(
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
        final var user = userFacade.getUser(jwt);

        return orderQueryRepository.findByUserUsername(user.getUsername());
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
    }

    List<OrderDto> findByParams(ZonedDateTime fromDate, ZonedDateTime toDate, String typeOfOrder, Boolean isReady, Long employeeId, String username) {
        return orderQueryRepository.findFilteredOrders(fromDate, toDate, typeOfOrder, isReady, employeeId, username);
    }

    private int calculatePrice(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .mapToInt(SimpleMenuItem::getPrice)
                .sum();
    }

    private OrderDto toDto(Order order) {
        return OrderDto.create(order.getId(), order.getPrice(), order.getHourOrder(), order.getHourAway(), order.getTypeOfOrder());
    }
}