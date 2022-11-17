package pl.jakubtworek.RestaurantManagementSystem.intTest.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderController;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.CooksQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.DeliveryQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.OrdersQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.business.queues.WaiterQueue;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Employee;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.employee.EmployeeFactory;
import pl.jakubtworek.RestaurantManagementSystem.model.factories.order.OrderFactory;
import pl.jakubtworek.RestaurantManagementSystem.repository.EmployeeRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.JobRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.OrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.repository.TypeOfOrderRepository;
import pl.jakubtworek.RestaurantManagementSystem.service.EmployeeService;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.TypeOfOrderService;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.EmployeeServiceImpl;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.OrderServiceImpl;
import pl.jakubtworek.RestaurantManagementSystem.service.impl.TypeOfOrderServiceImpl;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createEmployee;
import static pl.jakubtworek.RestaurantManagementSystem.utils.EmployeeUtils.createJob;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderRepository orderRepository;
    @MockBean
    private EmployeeRepository employeeRepository;
    @MockBean
    private JobRepository jobRepository;
    @MockBean
    private TypeOfOrderRepository typeOfOrderRepository;
    @Mock
    private OrderFactory orderFactory;
    @Mock
    private EmployeeFactory employeeFactory;
    @Mock
    private OrdersQueue ordersQueue;
    @Mock
    private CooksQueue cooksQueue;
    @Mock
    private WaiterQueue waiterQueue;
    @Mock
    private DeliveryQueue deliveryQueue;


    @Autowired
    private OrderService orderService;
    @Autowired
    private TypeOfOrderService typeOfOrderService;
    @Autowired
    private EmployeeService employeeService;
    @Autowired
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        mock(OrderRepository.class);
        mock(EmployeeRepository.class);
        mock(JobRepository.class);
        mock(TypeOfOrderRepository.class);
        mock(OrdersQueue.class);
        mock(WaiterQueue.class);
        mock(DeliveryQueue.class);
        mock(CooksQueue.class);

        typeOfOrderService = new TypeOfOrderServiceImpl(
                typeOfOrderRepository
        );
        employeeService = new EmployeeServiceImpl(
                employeeRepository,
                jobRepository,
                employeeFactory,
                cooksQueue,
                waiterQueue,
                deliveryQueue
        );
        orderService = new OrderServiceImpl(
                orderRepository,
                orderFactory,
                ordersQueue
        );
        orderController = new OrderController(
                orderService,
                typeOfOrderService,
                employeeService
        );
    }

    @Test
    void shouldReturnAllOrders() throws Exception {
        // given
        List<Order> expectedOrders = createOrders();

        // when
        when(orderRepository.findAll()).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

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
        Optional<Order> expectedOrder = createOnsiteOrder();

        // when
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        MvcResult mvcResult = mockMvc.perform(get("/orders/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        OrderResponse orderReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);

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
    void shouldReturnErrorResponse_whenAskedForNonExistingOrder() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/orders/id")
                        .param("id", String.valueOf(3L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no order in restaurant with that id: 3");
    }

    @Test
    void shouldReturnCreatedOrder() throws Exception {
        // given
        OrderRequest order = new OrderRequest(0L, "On-site", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));

        // when
        when(typeOfOrderRepository.findByType(eq("On-site"))).thenReturn(Optional.of(createOnsiteType()));

        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn();
        OrderResponse orderReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);

        // then
        assertEquals(12.98, orderReturned.getPrice());
        assertEquals("On-site", orderReturned.getTypeOfOrder().getType());
        assertEquals(2, orderReturned.getMenuItems().size());
    }

    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<Order> expectedOrder = createOnsiteOrder();

        // when
        when(orderRepository.findById(1L)).thenReturn(expectedOrder);

        MvcResult mvcResult = mockMvc.perform(delete("/orders/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted order has id: 1", response);
    }

    @Test
    void shouldReturnOrders_whenDateIsPassed() throws Exception {
        // given
        List<Order> expectedOrders = createOrders();

        // when
        when(orderRepository.findByDate(eq("2022-08-22"))).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders/find")
                        .param("date", "2022-08-22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals(2, ordersReturned.size());
    }

    @Test
    void shouldReturnOrders_whenTypeOfOrderIsPassed() throws Exception {
        // given
        TypeOfOrder expectedTypeOfOrder = createOnsiteType();
        List<Order> expectedOrders = List.of(createOnsiteOrder().get());

        // when
        when(typeOfOrderRepository.findByType(eq("On-site"))).thenReturn(Optional.of(expectedTypeOfOrder));
        when(orderRepository.findByTypeOfOrder(expectedTypeOfOrder)).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders/find")
                        .param("typeOfOrder", "On-site")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

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
    }

    @Test
    void shouldReturnOrders_whenEmployeeIsPassed() throws Exception {
        // given
        Optional<Employee> expectedEmployee = createEmployee();
        List<Order> expectedOrders = createOrders();

        // when
        when(employeeRepository.findById(1L)).thenReturn(expectedEmployee);
        when(orderRepository.findByEmployees(expectedEmployee.get())).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders/find")
                        .param("employeeId", String.valueOf(1L))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals(2, ordersReturned.size());
    }

    @Test
    void shouldReturnMadeOrders() throws Exception {
        List<Order> expectedOrders = List.of(createOnsiteOrder().get());
        // when
        when(orderRepository.findOrdersByHourAwayIsNotNull()).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders/ready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

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
    }

    @Test
    void shouldReturnUnmadeOrders() throws Exception {
        // given
        List<Order> expectedOrders = List.of(createDeliveryOrder().get());

        // when
        when(orderRepository.findOrdersByHourAwayIsNull()).thenReturn(expectedOrders);

        MvcResult mvcResult = mockMvc.perform(get("/orders/unready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertEquals(30.99, ordersReturned.get(0).getPrice());
        assertEquals("2022-08-22", ordersReturned.get(0).getDate());
        assertEquals("12:05", ordersReturned.get(0).getHourOrder());
        assertNull(ordersReturned.get(0).getHourAway());
        assertEquals("Delivery", ordersReturned.get(0).getTypeOfOrder().getType());
        assertEquals("Tiramisu", ordersReturned.get(0).getMenuItems().get(0).getName());
        assertEquals(5.99, ordersReturned.get(0).getMenuItems().get(0).getPrice());
        assertEquals("Coke", ordersReturned.get(0).getMenuItems().get(1).getName());
        assertEquals(1.99, ordersReturned.get(0).getMenuItems().get(1).getPrice());
        assertEquals("John", ordersReturned.get(0).getEmployees().get(0).getFirstName());
        assertEquals("Smith", ordersReturned.get(0).getEmployees().get(0).getLastName());
        assertEquals("Cook", ordersReturned.get(0).getEmployees().get(0).getJob().getName());
    }
}
