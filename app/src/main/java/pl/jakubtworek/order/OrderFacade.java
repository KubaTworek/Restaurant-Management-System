package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderItemDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public class OrderFacade {
    private final UserFacade userFacade;
    private final MenuItemFacade menuItemFacade;
    private final OrderQueryRepository orderQueryRepository;
    private final Order order;

    OrderFacade(final UserFacade userFacade,
                final MenuItemFacade menuItemFacade,
                final OrderQueryRepository orderQueryRepository,
                final Order order
    ) {
        this.userFacade = userFacade;
        this.menuItemFacade = menuItemFacade;
        this.orderQueryRepository = orderQueryRepository;
        this.order = order;
    }

    OrderDto save(OrderRequest toSave, String jwt) {
        final var user = userFacade.getByToken(jwt);
        final var items = getMenuItems(toSave.menuItems());

        final var created = order.from(
                items,
                toSave.typeOfOrder(),
                new UserId(user.getId())
        );

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

    private List<ItemDto> getMenuItems(List<String> names) {
        return names.stream()
                .map(menuItemFacade::getByName)
                .map(mi -> new ItemDto(mi.getName(), mi.getPrice()))
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