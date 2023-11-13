package pl.jakubtworek.order;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import pl.jakubtworek.DomainEventPublisher;
import pl.jakubtworek.auth.UserFacade;
import pl.jakubtworek.auth.dto.UserDto;
import pl.jakubtworek.auth.vo.UserId;
import pl.jakubtworek.employee.EmployeeFacade;
import pl.jakubtworek.menu.MenuItemFacade;
import pl.jakubtworek.menu.dto.MenuItemDto;
import pl.jakubtworek.order.dto.OrderDto;
import pl.jakubtworek.order.dto.OrderRequest;
import pl.jakubtworek.order.dto.Status;
import pl.jakubtworek.order.dto.TypeOfOrder;
import pl.jakubtworek.order.vo.OrderEvent;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OrderFacadeIT {
    @Mock
    private UserFacade userFacade;
    @Mock
    private EmployeeFacade employeeFacade;
    @Mock
    private MenuItemFacade menuItemFacade;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderQueryRepository orderQueryRepository;
    @Mock
    private DomainEventPublisher publisher;

    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        orderFacade = new OrderFacade(
                userFacade,
                employeeFacade,
                menuItemFacade,
                new OrderFactory(userFacade, menuItemFacade),
                orderRepository,
                orderQueryRepository,
                publisher
        );
    }

    @Test
    void shouldSaveOrderAndAddToQueue() {
        // given
        final var request = new OrderRequest("ON_SITE", List.of("Pizza"));
        final var expectedOrder = createOrder(1L, new BigDecimal("22.00"), ZonedDateTime.now(), null, TypeOfOrder.ON_SITE);
        final var user = UserDto.create(
                1L, "username", "password"
        );
        final var burger = MenuItemDto.create(1L, "Pizza", new BigDecimal("10.00"), Status.ACTIVE);

        when(userFacade.getByToken(any())).thenReturn(user);
        when(menuItemFacade.getByName(any())).thenReturn(burger);
        when(orderRepository.save(any())).thenReturn(expectedOrder);

        // when
        final var result = orderFacade.save(request, "jwt-token");

        // then
        verify(publisher).publish(any(OrderEvent.class));
        assertEquals(expectedOrder.getSnapshot().getId(), result.getId());
        assertEquals(expectedOrder.getSnapshot().getPrice(), result.getPrice());
    }

    @Test
    void shouldFindAllOrdersForUser() {
        // given
        final var expectedUser = UserDto.create(1L, "john.doe", "password");
        final var expectedOrders = createOrderDtos();

        when(userFacade.getByToken("jwt-token")).thenReturn(expectedUser);
        when(orderQueryRepository.findByUserId(1L)).thenReturn(expectedOrders);

        // when
        final List<OrderDto> result = orderFacade.findAllByToken("jwt-token");

        // then
        assertEquals(2, result.size());
    }

/*    @Test
    void shouldFindOrdersByParams() {
        // given
        final var expectedOrders = createOrderDtos();

        when(orderQueryRepository.findFilteredOrders(any(), any(), any(), any(), any(), any())).thenReturn(expectedOrders);

        // when
        final List<OrderResponse> result = orderFacade.findByParams(ZonedDateTime.now().toString(), ZonedDateTime.now().toString(), "ON_SITE", true, 1L, 1L);

        // then
        assertEquals(2, result.size());
    }*/

    private Order createOrder(Long id, BigDecimal price, ZonedDateTime hourOrder, ZonedDateTime hourAway, TypeOfOrder typeOfOrder) {
        return Order.restore(new OrderSnapshot(
                id, price, hourOrder, hourAway, typeOfOrder,
                new HashSet<>(), new HashSet<>(), new UserId(1L)
        ));
    }

    private List<OrderDto> createOrderDtos() {
        List<OrderDto> orderDtos = new ArrayList<>();
        orderDtos.add(OrderDto.create(1L, new BigDecimal("22.00"), ZonedDateTime.now(), null, TypeOfOrder.ON_SITE, new ArrayList<>()));
        orderDtos.add(OrderDto.create(2L, new BigDecimal("25.00"), ZonedDateTime.now(), null, TypeOfOrder.DELIVERY, new ArrayList<>()));
        return orderDtos;
    }
}
