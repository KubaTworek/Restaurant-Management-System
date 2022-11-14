package pl.jakubtworek.RestaurantManagementSystem.e2e;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class OrderE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllOrders() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrderById() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(delete("/orders/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted order has id: 1", response);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenDateIsPassed() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/orders/find")
                        .param("date", "2022-08-22")
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenTypeOfOrderIsPassed() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnOrders_whenEmployeeIsPassed() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnMadeOrders() throws Exception {
        // when
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnUnmadeOrders() throws Exception {
        // when
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
