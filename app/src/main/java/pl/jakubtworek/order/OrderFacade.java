package pl.jakubtworek.order;

import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderItemDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class OrderFacade {
    private static final String ORDER_NOT_FOUND_ERROR = "Order with that id doesn't exist";
    private final UserFacade userFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final DomainEventPublisher publisher;

    OrderFacade(final UserFacade userFacade,
                final MenuItemFacade menuItemFacade,
                final OrderRepository orderRepository,
                final OrderQueryRepository orderQueryRepository,
                final DomainEventPublisher publisher
    ) {
        this.userFacade = userFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderRepository = orderRepository;
        this.orderQueryRepository = orderQueryRepository;
        this.publisher = publisher;
    }

    public Order getById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new IllegalStateException(ORDER_NOT_FOUND_ERROR));
    }

    OrderDto save(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var menuItems = getMenuItems(toSave.menuItems());

        final var created = saveOrder(toSave, user.getId(), menuItems);

        publishOrderEvent(created, OrderEvent.State.TODO);
        return toDto(created);
    }

    List<OrderDto> findAllByToken(String jwt) {
        final var user = userFacade.getByToken(jwt);
        return orderQueryRepository.findDtoByClientId(new UserId(user.getId()));
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
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

    private Order saveOrder(OrderRequest toSave, Long userId, List<MenuItemDto> menuItems) {
        final var created = OrderFactory.createOrder(
                toSave.typeOfOrder(),
                userId,
                menuItems
        );
        return orderRepository.save(created);
    }

    private void publishOrderEvent(Order created, OrderEvent.State state) {
        publisher.publish(
                new OrderEvent(created.getSnapshot(1).getId(), null, created.getSnapshot(1).getTypeOfOrder(), created.getAmountOfMenuItems(), state)
        );
    }

    private List<MenuItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .toList();
    }

    private OrderDto toDto(Order order) {
        final var snap = order.getSnapshot(1);
        return OrderDto.create(snap.getId(), snap.getPrice(), snap.getHourOrder(), snap.getHourAway(), snap.getTypeOfOrder(), snap.getOrderItems().stream().map(this::toOrderItemDto).toList());
    }

    private OrderItemDto toOrderItemDto(OrderItemSnapshot snap) {
        return OrderItemDto.create(snap.getId(), snap.getName(), snap.getPrice(), snap.getAmount());
    }

    private ZonedDateTime parseDate(String dateStr) {
        return dateStr != null ? ZonedDateTime.parse(dateStr) : null;
    }

    private TypeOfOrder parseOrderType(String typeStr) {
        return typeStr != null ? TypeOfOrder.valueOf(typeStr) : null;
    }
}