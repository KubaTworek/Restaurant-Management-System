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
        assertEquals(12.99, ordersReturned.get(0).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(0).getDate());
        assertEquals("12:00", ordersReturned.get(0).getHourOrder());
        assertEquals("12:15", ordersReturned.get(0).getHourAway());
        assertEquals("On-site", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Chicken", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(10.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(0).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(0).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(0).getEmployees().get(0).getJob().getName());

        assertEquals(30.99, ordersReturned.get(1).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(1).getDate());
        assertEquals("12:05", ordersReturned.get(1).getHourOrder());
        assertNull(ordersReturned.get(1).getHourAway());
        assertEquals("Delivery", ordersReturned.get(1).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(1).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(1).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(1).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(1).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(1).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(1).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(1).getEmployees().get(0).getJob().getName());
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = Optional.of(createOnsiteOrder().convertEntityToDTO());

        // when
        when(orderService.findById(1L)).thenReturn(expectedOrder);

        OrderResponse orderReturned = orderController.getOrderById(1L).getBody();

        // then
        assertEquals(12.99, orderReturned.getPrice());
        assertEquals("2022-08-22", orderReturned.getDate());
        assertEquals("12:00", orderReturned.getHourOrder());
        assertEquals("12:15", orderReturned.getHourAway());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals("Chicken", orderReturned.getMenuItems().get(0).getName());
        assertEquals(10.99, orderReturned.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderReturned.getMenuItems().get(1).getName());
        assertEquals(1.99, orderReturned.getMenuItems().get(1).getPrice());
        assertEquals("John", orderReturned.getEmployees().get(0).getFirstName());
        assertEquals("Smith", orderReturned.getEmployees().get(0).getLastName());
        assertEquals("Cook", orderReturned.getEmployees().get(0).getJob().getName());
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingOrder() {
        // when
        Exception exception = assertThrows(OrderNotFoundException.class, () -> orderController.getOrderById(3L));

        // then
        assertEquals("There are no order in restaurant with that id: 3", exception.getMessage());
    }

    @Test
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest orderRequest = createOnsiteOrderRequest();
        OrderDTO order = createOnsiteOrder().convertEntityToDTO();

        // when
        when(orderService.save(any())).thenReturn(order);

        OrderResponse orderReturned = orderController.saveOrder(orderRequest).getBody();

        // then
        assertEquals(12.99, orderReturned.getPrice());
        assertEquals("2022-08-22", orderReturned.getDate());
        assertEquals("12:00", orderReturned.getHourOrder());
        assertEquals("12:15", orderReturned.getHourAway());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals("Chicken", orderReturned.getMenuItems().get(0).getName());
        assertEquals(10.99, orderReturned.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderReturned.getMenuItems().get(1).getName());
        assertEquals(1.99, orderReturned.getMenuItems().get(1).getPrice());
        assertEquals("John", orderReturned.getEmployees().get(0).getFirstName());
        assertEquals("Smith", orderReturned.getEmployees().get(0).getLastName());
        assertEquals("Cook", orderReturned.getEmployees().get(0).getJob().getName());
    }


    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = Optional.of(createOnsiteOrder().convertEntityToDTO());

        // when
        when(orderService.findById(1L)).thenReturn(expectedOrder);

        String response = orderController.deleteOrder(1L).getBody();

        // then
        assertEquals("Order with id: 1 was deleted", response);
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
        assertEquals(2, ordersReturned.size());
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
        assertEquals(2, ordersReturned.size());
    }
}
