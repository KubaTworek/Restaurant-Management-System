package pl.jakubtworek.order;

import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.vo.CustomerId;
import pl.jakubtworek.common.vo.Role;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.order.dto.ItemDto;
import pl.jakubtworek.order.dto.OrderConfirmRequest;
import pl.jakubtworek.order.dto.OrderDeliveryDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderItemDto;
import pl.jakubtworek.order.dto.OrderPriceDto;
import pl.jakubtworek.order.dto.OrderReceiveRequest;
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

        final var created = order.create(
                items,
                toSave.typeOfOrder(),
                new CustomerId(user.getId()),
                toSave.address()
        );

        return toDto(created);
    }

    OrderDto confirm(final OrderConfirmRequest orderRequest, final String jwt) {
        final var user = userFacade.getByToken(jwt);

        final var confirmed = order.confirm(
                orderRequest.orderId(),
                orderRequest.decision(),
                user.getId()
        );

        return toDto(confirmed);
    }

    List<OrderDto> findOngoingOrders(final String jwt) {
        final var user = userFacade.getByToken(jwt);

        return orderQueryRepository.findOngoingDtoByCustomerId(new CustomerId(user.getId()));
    }

    OrderDto receive(final OrderReceiveRequest orderRequest, final String jwt) {
        final var user = userFacade.getByToken(jwt);

        return toDto(order.receive(orderRequest.orderId(), orderRequest.tip(), user.getId()));
    }

    List<OrderDto> findAllByToken(String jwt) {
        final var user = userFacade.getByToken(jwt);
        return orderQueryRepository.findDtoByCustomerId(new CustomerId(user.getId()));
    }

    Optional<OrderDto> findById(Long id) {
        return orderQueryRepository.findDtoById(id);
    }

    List<OrderDto> findByParams(String fromDate,
                                String toDate,
                                String typeOfOrder,
                                Boolean isReady,
                                Long employeeId,
                                Long customerId,
                                String jwt
    ) {
        userFacade.verifyRole(jwt, Role.ADMIN);

        return orderQueryRepository.findFilteredOrders(
                parseDate(fromDate),
                parseDate(toDate),
                parseOrderType(typeOfOrder),
                isReady,
                employeeId,
                customerId
        );
    }

    private List<ItemDto> getMenuItems(List<String> names) {
        final var menuItems = menuItemFacade.getByNames(names);

        return menuItems.stream()
                .map(mi -> new ItemDto(mi.getName(), mi.getPrice()))
                .toList();
    }

    private OrderDto toDto(Order order) {
        final var snap = order.getSnapshot(1);
        final var price = snap.getPrice();
        return OrderDto.create(snap.getId(), OrderPriceDto.create(price.getId(), price.getPrice(), price.getDeliveryFee(), price.getMinimumBasketFee(), price.getTip()), snap.getHourOrder(), snap.getHourPrepared(), snap.getHourReceived(), snap.getTypeOfOrder(), snap.getStatus(), snap.getDelivery() != null ? OrderDeliveryDto.create(snap.getDelivery().getId(), snap.getDelivery().getHourStart(), snap.getDelivery().getHourEnd(), snap.getDelivery().getStatus(), snap.getDelivery().getDistrict(), snap.getDelivery().getStreet(), snap.getDelivery().getHouseNumber()) : null, snap.getOrderItems().stream().map(this::toOrderItemDto).toList());
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