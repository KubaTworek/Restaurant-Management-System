package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.employee.dto.EmployeeDto;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.menu.dto.SimpleMenuItem;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.SimpleOrder;
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
        order.addEmployee(employee);
        orderRepository.save(order);
    }

    public int getNumberOfMenuItems(SimpleOrder order) {
        return getOrderById(order.getId())
                .getAmountOfMenuItems();
    }

    OrderDto save(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var menuItems = getMenuItems(toSave.getMenuItems());

        final var order = new Order();
        order.updateInfo(
                menuItems,
                toSave.getTypeOfOrder(),
                user
        );

        final var created = orderRepository.save(order);

        publisher.publish(created.sendToKitchen());

        return toDto(created);
    }

    List<OrderDto> findAll(String jwt) {
        final var user = userFacade.getByToken(jwt);
        return orderQueryRepository.findByUserUsername(user.getUsername());
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
    }

    public List<OrderDto> findByParams(String fromDateStr, String toDateStr, String typeOfOrder, Boolean isReady, Long employeeId, String username) {
        final var fromDate = parseDate(fromDateStr);
        final var toDate = parseDate(toDateStr);
        final var orderType = parseOrderType(typeOfOrder);
        return orderQueryRepository.findFilteredOrders(fromDate, toDate, orderType, isReady, employeeId, username);
    }

    private List<SimpleMenuItem> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .collect(Collectors.toList());
    }

    private OrderDto toDto(Order order) {
        var snap = order.getSnapshot();

        final var employeeDtos = snap.getEmployees().stream()
                .map(employee -> EmployeeDto.create(employee.getId(), employee.getFirstName(), employee.getLastName(), employee.getJob()))
                .collect(Collectors.toList());

        final var menuItemDtos = snap.getMenuItems().stream()
                .map(menuItem -> MenuItemDto.create(menuItem.getId(), menuItem.getName(), menuItem.getPrice()))
                .collect(Collectors.toList());

        return OrderDto.create(snap.getId(), snap.getPrice(), snap.getHourOrder(), snap.getHourAway(), snap.getTypeOfOrder(), employeeDtos, menuItemDtos);
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