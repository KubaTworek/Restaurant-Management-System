package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.employee.vo.EmployeeId;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.vo.MenuItemId;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.OrderResponse;
import pl.jakubtworek.order.dto.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class OrderFacade {
    private static final String ORDER_NOT_FOUND_ERROR = "Order with that id doesn't exist";
    private final UserFacade userFacade;
    private final EmployeeFacade employeeFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final DomainEventPublisher publisher;

    OrderFacade(final UserFacade userFacade, final EmployeeFacade employeeFacade, final MenuItemFacade menuItemFacade,
                final OrderRepository orderRepository, final OrderQueryRepository orderQueryRepository,
                final DomainEventPublisher publisher) {
        this.userFacade = userFacade;
        this.employeeFacade = employeeFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.publisher = publisher;
    }

    public void setAsDelivered(Long orderId) {
        final var order = getOrderById(orderId);
        order.delivery();
        orderRepository.save(order);
    }

    public void addEmployeeToOrder(Long orderId, Long employeeId) {
        final var order = getOrderById(orderId);
        final var employee = employeeFacade.getById(employeeId);
        order.addEmployee(new EmployeeId(employee.getId()));
        orderRepository.save(order);
    }

    OrderResponse save(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var menuItems = getMenuItems(toSave.getMenuItems());

        final var order = new Order();
        order.updateInfo(
                menuItems.stream().map(MenuItemDto::getId).map(MenuItemId::new).collect(Collectors.toSet()),
                calculatePrice(menuItems),
                toSave.getTypeOfOrder(),
                new UserId(user.getId())
        );

        final var created = orderRepository.save(order);

        publisher.publish(created.sendToKitchen());

        return toResponse(created);
    }

    private int calculatePrice(Set<MenuItemDto> menuItems) {
        return menuItems.stream()
                .mapToInt(MenuItemDto::getPrice)
                .sum();
    }

    List<OrderDto> findAll(String jwt) {
        final var user = userFacade.getByToken(jwt);
        return orderQueryRepository.findByUserId(user.getId());
    }

    Optional<OrderResponse> findById(Long id) {
        return orderRepository.findById(id).map(this::toResponse);
    }

    public List<OrderDto> findByParams(String fromDateStr, String toDateStr, String typeOfOrder, Boolean isReady, Long employeeId, Long userId) {
        final var fromDate = parseDate(fromDateStr);
        final var toDate = parseDate(toDateStr);
        final var orderType = parseOrderType(typeOfOrder);
        return orderQueryRepository.findFilteredOrders(fromDate, toDate, orderType, isReady, employeeId, userId);
    }

    private Set<MenuItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .collect(Collectors.toSet());
    }

    private OrderResponse toResponse(Order order) {
        var snap = order.getSnapshot();

        final var menuItemDtos = snap.getMenuItems().stream()
                .map(mi -> menuItemFacade.getById(mi.getId()))
                .map(menuItem -> MenuItemDto.create(menuItem.getId(), menuItem.getName(), menuItem.getPrice()))
                .collect(Collectors.toList());

        final var employeeDtos = snap.getEmployees().stream()
                .map(mi -> employeeFacade.getById(mi.getId()))
                .map(employee -> EmployeeDto.create(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getJob()))
                .collect(Collectors.toList());

        return new OrderResponse(snap.getId(), snap.getPrice(), snap.getHourOrder(), snap.getHourAway(), snap.getTypeOfOrder(), menuItemDtos, employeeDtos);
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