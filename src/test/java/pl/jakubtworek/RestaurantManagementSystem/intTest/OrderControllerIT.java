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
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.GetOrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
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

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnAllOrders() throws Exception {
        mockMvc.perform(get("/orders"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
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
        assertThat(orderReturned.getEmployees().size()).isEqualTo(1);
        assertThat(orderReturned.getTypeOfOrder().getType()).isEqualTo("On-site");
        assertThat(orderReturned.getMenuItems().size()).isEqualTo(2);
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
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
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponseConfirmingDeletedOrder() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/orders/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted order has id: 1");
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenDateIsPassed() throws Exception {
        mockMvc.perform(get("/orders/date/2022-08-22")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnEmployees_whenTypeOfOrderIsPassed() throws Exception {
        mockMvc.perform(get("/orders/type-of-order/On-site")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnMadeOrders() throws Exception {
        mockMvc.perform(get("/orders/ready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnUnmadeOrders() throws Exception {
        mockMvc.perform(get("/orders/unready")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(0)));
    }
}
