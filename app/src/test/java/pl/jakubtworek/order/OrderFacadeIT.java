package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.common.vo.Status;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.vo.OrderEvent;
import pl.jakubtworek.order.vo.TypeOfOrder;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFacadeIT {
    @Mock
    private UserFacade userFacade;
    @Mock
    private MenuItemFacade menuItemFacade;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderQueryRepository orderQueryRepository;
    @Mock
    private DomainEventPublisher publisher;

    private OrderFacade orderFacade;

    private final Order order = createOrder();
    private final OrderDto orderDto = OrderDto.create(1L, new BigDecimal("22.00"), ZonedDateTime.now(), null, TypeOfOrder.ON_SITE, new ArrayList<>());
    private final List<OrderDto> orders = createOrderDtos();
    private final UserDto user = UserDto.create(1L, "username", "password");
    private final MenuItemDto burger = MenuItemDto.create(1L, "Pizza", new BigDecimal("10.00"), Status.ACTIVE);

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        orderFacade = new OrderFacade(
                userFacade,
                menuItemFacade,
                orderRepository,
                orderQueryRepository,
                publisher
        );
        when(userFacade.getByToken("jwt-token")).thenReturn(user);
        when(menuItemFacade.getByName(any())).thenReturn(burger);
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(orderRepository.findById(2L)).thenReturn(Optional.empty());
        when(orderRepository.save(any())).thenReturn(order);
        when(orderQueryRepository.findDtoById(any())).thenReturn(Optional.of(orderDto));
        when(orderQueryRepository.findDtoByClientId(any())).thenReturn(orders);
        when(orderQueryRepository.findFilteredOrders(any(), any(), any(), any(), any(), any())).thenReturn(orders);
    }

    @Test
    void shouldGetById_whenOrderExist() {
        // when
        final var result = orderFacade.getById(1L);

        // then
        assertEquals(order, result);
    }

    @Test
    void shouldNotGetById_whenOrderDoesNotExist() {
        // when & then
        assertThrows(IllegalStateException.class, () -> orderFacade.getById(2L));
    }

    @Test
    void shouldSaveOrderAndAddToQueue() {
        // given
        final var request = new OrderRequest("ON_SITE", List.of("Pizza"));

        // when
        final var result = orderFacade.save(request, "jwt-token");

        // then
        verify(publisher).publish(any(OrderEvent.class));
        assertEquals(order.getSnapshot(1).getId(), result.getId());
    }

    @Test
    void shouldFindAllOrdersByToken() {
        // when
        final var result = orderFacade.findAllByToken("jwt-token");

        // then
        assertEquals(2, result.size());
    }

    @Test
    void shouldFindById() {
        // when
        final var result = orderFacade.findById(1L);

        // then
        assertEquals(Optional.of(orderDto), result);
    }

    @Test
    void shouldFindOrdersByParams() {
        // when
        final var result = orderFacade.findByParams(ZonedDateTime.now().toString(), ZonedDateTime.now().toString(), "ON_SITE", true, 1L, 1L);

        // then
        assertEquals(2, result.size());
    }

    private Order createOrder() {
        return Order.restore(new OrderSnapshot(
                1L, BigDecimal.valueOf(22.00), ZonedDateTime.now(), null, TypeOfOrder.ON_SITE,
                new HashSet<>(), new HashSet<>(), new UserId(1L)
        ), 1);
    }

    private List<OrderDto> createOrderDtos() {
        List<OrderDto> orders = new ArrayList<>();
        orders.add(OrderDto.create(1L, new BigDecimal("22.00"), ZonedDateTime.now(), null, TypeOfOrder.ON_SITE, new ArrayList<>()));
        orders.add(OrderDto.create(2L, new BigDecimal("25.00"), ZonedDateTime.now(), null, TypeOfOrder.DELIVERY, new ArrayList<>()));
        return orders;
    }
}
