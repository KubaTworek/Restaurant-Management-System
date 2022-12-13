package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.*;
import pl.jakubtworek.RestaurantManagementSystem.exception.OrderNotFoundException;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

class OrderControllerUserTest {
    @Mock
    private OrderService orderService;
    @Mock
    private Authentication authentication;
    @Mock
    private SecurityContext securityContext;

    private OrderControllerUser orderController;

    @BeforeEach
    void setup() {
        orderService = mock(OrderService.class);
        authentication = mock(Authentication.class);
        securityContext = mock(SecurityContext.class);

        orderController = new OrderControllerUser(
                orderService
        );

        when(securityContext.getAuthentication()).thenReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
        when(authentication.getName()).thenReturn("user");
    }

    @Test
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest orderRequest = createOnsiteOrderRequest();
        OrderDTO orderDTO = createOnsiteOrder().convertEntityToDTO();

        // when
        when(orderService.save(orderRequest)).thenReturn(orderDTO);

        OrderResponse orderCreated = orderController.saveOrder(orderRequest).getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrder(orderCreated);
    }

    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = Optional.of(createOnsiteOrder().convertEntityToDTO());

        // when
        when(orderService.findByIdAndUsername(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"), "user")).thenReturn(expectedOrder);

        String response = orderController.deleteOrder(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).getBody();

        // then
        assertEquals("Order with id: 8e4087ce-7846-11ed-a1eb-0242ac120002 was deleted", response);
    }

    @Test
    void shouldReturnAllOrders() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findAllByUsername("user")).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrders().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = Optional.of(createOnsiteOrder().convertEntityToDTO());

        // when
        when(orderService.findByIdAndUsername(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"), "user")).thenReturn(expectedOrder);

        OrderResponse orderReturned = orderController.getOrderById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002")).getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrder(orderReturned);
    }

    @Test
    void shouldThrowException_whenOrderNotExist() {
        // when
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(UUID.fromString("a0f7ae28-7847-11ed-a1eb-0242ac120002")));

        // then
        assertEquals("There are no order in restaurant with that id: a0f7ae28-7847-11ed-a1eb-0242ac120002", exception.getMessage());
    }

    @Test
    void shouldReturnMadeOrders() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findMadeOrdersAndUsername("user")).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderMade().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnUnmadeOrders() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findUnmadeOrdersAndUsername("user")).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderUnmade().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }
}