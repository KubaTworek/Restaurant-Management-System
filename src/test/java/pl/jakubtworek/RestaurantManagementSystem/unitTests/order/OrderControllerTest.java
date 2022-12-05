package pl.jakubtworek.RestaurantManagementSystem.unitTests.order;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.*;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.dto.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.service.OrderService;

import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static pl.jakubtworek.RestaurantManagementSystem.utils.OrderUtils.*;

;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private OrderService orderService;

    @Test
    void shouldReturnAllOrders() throws Exception {
        // given
        List<OrderDTO> expectedOrders = createOrdersDTO();

        // when
        when(orderService.findAll()).thenReturn(expectedOrders);

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
        Optional<OrderDTO> expectedOrder = createOnsiteOrderDTO();

        // when
        when(orderService.findById(1L)).thenReturn(expectedOrder);

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
        assertEquals(404, response.getStatus());
        assertEquals("There are no order in restaurant with that id: 3", response.getMessage());
    }

/*    @Test
    @Sql(statements = {"INSERT INTO `type_of_order` VALUES (1,'On-site'), (2,'Delivery')", "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')", "INSERT INTO `menu_item`(id,name,price,menu_id) VALUES (1,'Chicken',10.99,2), (2,'Coke',1.99,1), (3,'Tiramisu',5.99,2)"})
    void shouldReturnCreatedOrder() throws Exception {
        GetMenuItemDTO menuItem = new GetMenuItemDTO(4L, "Coke", 2.99, "Drinks");
        GetOrderDTO order = new GetOrderDTO(0L, "On-site", List.of(menuItem));

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn();
        OrderDTO orderReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderDTO.class);

        assertThat(orderReturned).isNotNull();
        assertThat(orderReturned.getId()).isNotNull();
        assertThat(orderReturned.getHourAway()).isNull();
        assertThat(orderReturned.getHourOrder()).isNotNull();
        assertThat(orderReturned.getPrice()).isEqualTo(2.99);
        assertThat(orderReturned.getDate()).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(orderReturned.getEmployees().size()).isEqualTo(1);
        assertThat(orderReturned.getTypeOfOrder().getType()).isEqualTo("RandomType");
        assertThat(orderReturned.getMenuItems().size()).isEqualTo(1);
    }*/

    @Test
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // given
        Optional<OrderDTO> expectedOrder = createOnsiteOrderDTO();

        // when
        when(orderService.findById(1L)).thenReturn(expectedOrder);

        MvcResult mvcResult = mockMvc.perform(delete("/orders/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        OrderResponse orderDeleted = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderResponse.class);

        // then
        assertEquals(12.99, orderDeleted.getPrice());
        assertEquals("2022-08-22", orderDeleted.getDate());
        assertEquals("12:00", orderDeleted.getHourOrder());
        assertEquals("12:15", orderDeleted.getHourAway());
        assertEquals("On-site", orderDeleted.getTypeOfOrder().getType());
        assertEquals("Chicken", orderDeleted.getMenuItems().get(0).getName());
        assertEquals(10.99, orderDeleted.getMenuItems().get(0).getPrice());
        assertEquals("Coke", orderDeleted.getMenuItems().get(1).getName());
        assertEquals(1.99, orderDeleted.getMenuItems().get(1).getPrice());
        assertEquals("John", orderDeleted.getEmployees().get(0).getFirstName());
        assertEquals("Smith", orderDeleted.getEmployees().get(0).getLastName());
        assertEquals("Cook", orderDeleted.getEmployees().get(0).getJob().getName());
    }

/*    @Test
    void shouldReturnOrders_whenDateIsPassed() throws Exception {
        // given
        List<OrderDTO> expectedOrders = createOrdersDTO();

        // when
        when(orderService.findByDate(eq("2022-08-22"))).thenReturn(expectedOrders);

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
        TypeOfOrderDTO expectedTypeOfOrderDTO = createOnsiteTypeDTO();
        TypeOfOrder expectedTypeOfOrder = createOnsiteType();
        List<OrderDTO> expectedOrders = List.of(createOnsiteOrderDTO().get());

        // when
        when(typeOfOrderService.findByType(eq("On-site"))).thenReturn(Optional.of(expectedTypeOfOrderDTO));
        when(orderService.findByTypeOfOrder(expectedTypeOfOrder)).thenReturn(expectedOrders);

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
        Optional<EmployeeDTO> expectedEmployee = createEmployeeDTO();
        Employee providedEmployee = createEmployee().get();
        List<OrderDTO> expectedOrders = createOrdersDTO();

        // when
        when(employeeService.findById(1L)).thenReturn(expectedEmployee);
        when(orderService.findByEmployee(providedEmployee)).thenReturn(expectedOrders);

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
    }*/

    @Test
    void shouldReturnMadeOrders() throws Exception {
        // given
        List<OrderDTO> expectedOrders = List.of(createOnsiteOrderDTO().get());

        // when
        when(orderService.findMadeOrders()).thenReturn(expectedOrders);

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
        List<OrderDTO> expectedOrders = List.of(createDeliveryOrderDTO().get());

        // when
        when(orderService.findUnmadeOrders()).thenReturn(expectedOrders);

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