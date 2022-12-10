package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
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

class OrderControllerTest {
    @Mock
    private OrderService orderService;

    private OrderController orderController;

    @BeforeEach
    void setup() {
        orderService = mock(OrderService.class);

        orderController = new OrderController(
                orderService
        );
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
        when(orderService.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(expectedOrder);

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
        when(orderService.findAll()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrders().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = Optional.of(createOnsiteOrder().convertEntityToDTO());

        // when
        when(orderService.findById(UUID.fromString("8e4087ce-7846-11ed-a1eb-0242ac120002"))).thenReturn(expectedOrder);

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

/*    @Test
    void shouldReturnOrders_whenDateIsPassed() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findByDate(eq("2022-08-22"))).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams("2022-08-22", null, null).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenTypeOfOrderIsPassed() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());
        TypeOfOrder typeOfOrder = createOnsiteType();

        // when
        when(typeOfOrderService.findByType(anyString())).thenReturn(Optional.of(typeOfOrder.convertEntityToDTO()));
        when(orderService.findByTypeOfOrder(any())).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(null, "On-site", null).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenEmployeeIdIsPassed() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findByEmployeeId(1L)).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderByParams(null, null, 1L).getBody();

        // then
        assertEquals(2, ordersReturned.size());
    }*/


    @Test
    void shouldReturnMadeOrders() {
        // given
        List<OrderDTO> expectedOrders = createOrders()
                .stream()
                .map(Order::convertEntityToDTO)
                .collect(Collectors.toList());

        // when
        when(orderService.findMadeOrders()).thenReturn(expectedOrders);

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
        when(orderService.findUnmadeOrders()).thenReturn(expectedOrders);

        List<OrderResponse> ordersReturned = orderController.getOrderUnmade().getBody();

        // then
        OrderResponseAssertions.checkAssertionsForOrders(ordersReturned);
    }
}
