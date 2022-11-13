package pl.jakubtworek.RestaurantManagementSystem.controller.order;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemController;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.TypeOfOrder;
import pl.jakubtworek.RestaurantManagementSystem.service.*;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockBean
    private OrderService orderService;
    @MockBean
    private TypeOfOrderService typeOfOrderService;
    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private OrderController orderController;

    @BeforeEach
    public void setup() {
        mock(OrderService.class);
        mock(MenuService.class);

        orderController = new OrderController(
                orderService,
                typeOfOrderService,
                employeeService
        );

        when(orderService.findAll()).thenReturn(List.of((spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder()), List.of(), List.of())))));
        when(orderService.findByTypeOfOrder(any(TypeOfOrder.class))).thenReturn(List.of((spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder()), List.of(), List.of())))));
        when(typeOfOrderService.findByType("On-site")).thenReturn(Optional.of(spy(new TypeOfOrder(1L, "On-site", List.of()))));
        when(orderService.findByDate(eq("2022-08-22"))).thenReturn(List.of((spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder()), List.of(), List.of())))));
        when(orderService.findMadeOrders()).thenReturn(List.of((spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder()), List.of(), List.of())))));
        when(orderService.findUnmadeOrders()).thenReturn(List.of((spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder()), List.of(), List.of())))));
        when(orderService.findById(1L)).thenReturn(Optional.of(spy(new Order(1L, 12.99, "2022-08-22", "12:00", "12:15", spy(new TypeOfOrder(1L, "On-site", List.of())), List.of(), List.of()))));
        when(orderService.checkIfOrderIsNull(3L)).thenReturn(true);


    }


    @Test
    void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnOrderById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/1"))
                .andExpect(status().is(200))
                .andReturn();
        OrderDTO orderReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), OrderDTO.class);

        assertThat(orderReturned).isNotNull();
        assertThat(orderReturned.getId()).isEqualTo(1L);
        assertThat(orderReturned.getHourAway()).isEqualTo("12:15");
        assertThat(orderReturned.getHourOrder()).isEqualTo("12:00");
        assertThat(orderReturned.getPrice()).isEqualTo(12.99);
        assertThat(orderReturned.getDate()).isEqualTo("2022-08-22");
        assertThat(orderReturned.getTypeOfOrder().getType()).isEqualTo("On-site");
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingOrder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/3"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no order in restaurant with that id: 3");
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
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted order has id: 1");
    }

    @Test
    void shouldReturnEmployees_whenDateIsPassed() throws Exception {
        mockMvc.perform(get("/orders/date/2022-08-22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnEmployees_whenTypeOfOrderIsPassed() throws Exception {
        mockMvc.perform(get("/orders/type-of-order/On-site")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnMadeOrders() throws Exception {
        mockMvc.perform(get("/orders/ready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void shouldReturnUnmadeOrders() throws Exception {
        mockMvc.perform(get("/orders/unready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }
}
