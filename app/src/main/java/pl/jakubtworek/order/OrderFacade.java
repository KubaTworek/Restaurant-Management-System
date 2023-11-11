package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.OrderResponse;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class OrderFacade {
    private static final String ORDER_NOT_FOUND_ERROR = "Order with that id doesn't exist";
    private final UserFacade userFacade;
    private final EmployeeFacade employeeFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderFactory orderFactory;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final Kitchen kitchen;

    OrderFacade(final UserFacade userFacade,
                final EmployeeFacade employeeFacade,
                final MenuItemFacade menuItemFacade,
                final OrderFactory orderFactory,
                final OrderRepository orderRepository,
                final OrderQueryRepository orderQueryRepository,
                final Kitchen kitchen
    ) {
        this.userFacade = userFacade;
        this.employeeFacade = employeeFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderFactory = orderFactory;
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.kitchen = kitchen;
    }

    public void setAsDelivered(Long orderId) {
        final var order = getOrderById(orderId);
        order.delivery();
        orderRepository.save(order);
    }

    public void addEmployeeToOrder(Long orderId, Long employeeId) {
        final var order = getOrderById(orderId);
        final var employee = employeeFacade.getById(employeeId);
        order.addEmployee(
                new EmployeeId(employee.getId())
        );
        orderRepository.save(order);
    }

    OrderResponse save(OrderRequest toSave, String jwt) {
        final var created = orderFactory.createOrder(toSave, jwt);
        kitchen.handle(created);
        return toResponse(created);
    }

    List<OrderDto> findAllByToken(String jwt) {
        final var user = userFacade.getByToken(jwt);
        return orderQueryRepository.findByUserId(user.getId());
    }

    Optional<OrderResponse> findById(Long id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    List<OrderDto> findByParams(String fromDate,
                                String toDate,
                                String typeOfOrder,
                                Boolean isReady,
                                Long employeeId,
                                Long userId
    ) {
        return orderQueryRepository.findFilteredOrders(
                parseDate(fromDate),
                parseDate(toDate),
                parseOrderType(typeOfOrder),
                isReady,
                employeeId,
                userId
        );
    }

    private OrderResponse toResponse(Order order) {
        final var snap = order.getSnapshot();

        final var menuItems = snap.getMenuItems().stream()
                .map(mi -> menuItemFacade.getById(mi.getId()))
                .map(menuItem -> MenuItemDto.create(
                        menuItem.getId(), menuItem.getName(), menuItem.getPrice())
                )
                .collect(Collectors.toList());

        final var employees = snap.getEmployees().stream()
                .map(e -> employeeFacade.getById(e.getId()))
                .map(employee -> EmployeeDto.create(
                        employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getJob())
                )
                .collect(Collectors.toList());

        return new OrderResponse(
                snap.getId(), snap.getPrice(), snap.getHourOrder(), snap.getHourAway(), snap.getTypeOfOrder(), menuItems, employees
        );
    }

    private Order getOrderById(Long orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new IllegalStateException(ORDER_NOT_FOUND_ERROR));
    }

    private ZonedDateTime parseDate(String dateStr) {
        return dateStr != null ? ZonedDateTime.parse(dateStr) : null;
    }

    private TypeOfOrder parseOrderType(String typeStr) {
        return typeStr != null ? TypeOfOrder.valueOf(typeStr) : null;
    }
}