package pl.jakubtworek.RestaurantManagementSystem.intTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.JobDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.TypeOfOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.InstanceOfAssertFactories.LOCAL_DATE;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.employees}")
    private String sqlAddEmployees;

    @Value("${sql.script.create.jobs}")
    private String sqlAddJobs;

    @Value("${sql.script.create.typeoforder}")
    private String sqlAddTypeOfOrder;

    @Value("${sql.script.create.order}")
    private String sqlAddOrder;

    @Value("${sql.script.create.menuitem}")
    private String sqlAddMenuItem;

    @Value("${sql.script.create.order_employee}")
    private String sqlAddOrder_Employee;

    @Value("${sql.script.create.order_menuitem}")
    private String sqlAddOrder_MenuItem;


    @Value("${sql.script.delete.employees}")
    private String sqlDeleteEmployees;

    @Value("${sql.script.delete.jobs}")
    private String sqlDeleteJobs;

    @Value("${sql.script.delete.typeoforder}")
    private String sqlDeleteTypeOfOrder;

    @Value("${sql.script.delete.order}")
    private String sqlDeleteOrder;

    @Value("${sql.script.delete.menuitem}")
    private String sqlDeleteMenuItem;

    @Value("${sql.script.delete.order_employee}")
    private String sqlDeleteOrder_Employee;

    @Value("${sql.script.delete.order_menuitem}")
    private String sqlDeleteOrder_MenuItem;

    @BeforeEach
    void setup() {
        jdbc.execute(sqlAddJobs);
        jdbc.execute(sqlAddEmployees);
        jdbc.execute(sqlAddTypeOfOrder);
        jdbc.execute(sqlAddOrder);
        jdbc.execute(sqlAddMenuItem);
        jdbc.execute(sqlAddOrder_Employee);
        jdbc.execute(sqlAddOrder_MenuItem);
    }

    @AfterEach
    void delete() {
        jdbc.execute(sqlDeleteEmployees);
        jdbc.execute(sqlDeleteJobs);
        jdbc.execute(sqlDeleteTypeOfOrder);
        jdbc.execute(sqlDeleteOrder);
        jdbc.execute(sqlDeleteMenuItem);
        jdbc.execute(sqlDeleteOrder_Employee);
        jdbc.execute(sqlDeleteOrder_MenuItem);
    }

    @Test
    void getOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getOrderById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/1"))
                .andExpect(status().is(200))
                .andReturn();
        Order order = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);

        assertThat(order).isNotNull();
        assertThat(order.getId()).isEqualTo(1L);
        assertThat(order.getHourAway()).isEqualTo("12:15:00");
        assertThat(order.getHourOrder()).isEqualTo("12:00:00");
        assertThat(order.getPrice()).isEqualTo(12.99);
        assertThat(order.getDate()).isEqualTo("2022-08-22");
        assertThat(order.getEmployees().size()).isEqualTo(1);
        assertThat(order.getTypeOfOrder().getType()).isEqualTo("On-site");
        assertThat(order.getMenuItems().size()).isEqualTo(2);
    }

    @Test
    void getOrderByWrongId() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/orders/3"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no order in restaurant with that id: 3");
    }

    @Test
    void saveOrder() throws Exception {
        JobDTO job = new JobDTO(3L, "Cleaner");
        EmployeeDTO employee = new EmployeeDTO(4L, "James", "Smith", job);
        List<EmployeeDTO> employeeList= List.of(employee);
        MenuItemDTO menuItem = new MenuItemDTO(4L, "Coke", 2.99);
        List<MenuItemDTO> menuItemList= List.of(menuItem);
        MenuDTO menu = new MenuDTO(3L, "Cold-Drinks", menuItemList);
        TypeOfOrderDTO typeOfOrderDTO = new TypeOfOrderDTO(3L, "RandomType");
        OrderDTO order = new OrderDTO(3L, 0, "", null, null, typeOfOrderDTO, menuItemList, employeeList);

        MvcResult mvcResult = mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)))
                .andExpect(status().isCreated())
                .andReturn();
        Order orderGet = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), Order.class);

        assertThat(orderGet).isNotNull();
        assertThat(orderGet.getId()).isNotNull();
        assertThat(orderGet.getHourAway()).isNull();
        assertThat(orderGet.getHourOrder()).isNotNull();
        assertThat(orderGet.getPrice()).isEqualTo(2.99);
        assertThat(orderGet.getDate()).isEqualTo(LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
        assertThat(orderGet.getEmployees().size()).isEqualTo(1);
        assertThat(orderGet.getTypeOfOrder().getType()).isEqualTo("RandomType");
        assertThat(orderGet.getMenuItems().size()).isEqualTo(1);
    }

    @Test
    void deleteOrder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted order has id: 1");
    }

    @Test
    void getOrdersByDate() throws Exception {
        mockMvc.perform(get("/orders/date/2022-08-22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getOrdersByTypeOfOrder() throws Exception {
        mockMvc.perform(get("/orders/type-of-order/On-site")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void getOrdersReady() throws Exception {
        mockMvc.perform(get("/orders/ready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void getOrdersUnready() throws Exception {
        mockMvc.perform(get("/orders/unready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
