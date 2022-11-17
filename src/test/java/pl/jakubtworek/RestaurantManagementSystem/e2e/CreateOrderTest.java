package pl.jakubtworek.RestaurantManagementSystem.e2e;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.employee.EmployeeResponse;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.order.OrderResponse;
import pl.jakubtworek.RestaurantManagementSystem.model.entity.Order;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class CreateOrderTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        EmployeeRequest cook = new EmployeeRequest(0L, "James", "Morgan", "Cook");
        EmployeeRequest waiter = new EmployeeRequest(0L, "James", "Morgan", "Waiter");
        EmployeeRequest delivery = new EmployeeRequest(0L, "James", "Morgan", "DeliveryMan");


        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(cook)));

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(waiter)));
        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(delivery)));
    }

    @Test
    @Sql({"/deleting-data.sql"})
    @Sql(statements = {"INSERT INTO `type_of_order` VALUES (1,'On-site'), (2,'Delivery')", "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')", "INSERT INTO `menu_item`(id,name,price,menu_id) VALUES (1,'Chicken',10.99,2), (2,'Coke',1.99,1), (3,'Tiramisu',5.99,2)", "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')"})
    void shouldCheckIsOrderIsDeliveringByWaiter() throws Exception {
        // given
        OrderRequest order = new OrderRequest(0L, "On-site", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(order)));

        MvcResult mvcResult1 = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertNull(ordersReturned1.get(0).getHourAway());

        // when
        Thread.sleep(6000);

        MvcResult mvcResult2 = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertNotNull(ordersReturned2.get(0).getHourAway());
    }

    @Test
    @Sql({"/deleting-data.sql"})
    @Sql(statements = {"INSERT INTO `type_of_order` VALUES (1,'On-site'), (2,'Delivery')", "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')", "INSERT INTO `menu_item`(id,name,price,menu_id) VALUES (1,'Chicken',10.99,2), (2,'Coke',1.99,1), (3,'Tiramisu',5.99,2)", "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')"})
    void shouldCheckIsOrderIsDeliveringByDeliveryMan() throws Exception {
        // given
        OrderRequest order = new OrderRequest(0L, "Delivery", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));

        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(order)));

        MvcResult mvcResult1 = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned1 = objectMapper.readValue(mvcResult1.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertNull(ordersReturned1.get(0).getHourAway());

        // when
        Thread.sleep(8000);

        MvcResult mvcResult2 = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andReturn();
        List<OrderResponse> ordersReturned2 = objectMapper.readValue(mvcResult2.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertNotNull(ordersReturned2.get(0).getHourAway());
    }

    @Test
    @Sql({"/deleting-data.sql"})
    @Sql(statements = {"INSERT INTO `type_of_order` VALUES (1,'On-site'), (2,'Delivery')", "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')", "INSERT INTO `menu_item`(id,name,price,menu_id) VALUES (1,'Chicken',10.99,2), (2,'Coke',1.99,1), (3,'Tiramisu',5.99,2)", "INSERT INTO `job` VALUES (1, 'Cook'), (2, 'Waiter'), (3, 'DeliveryMan')"})
    void shouldCheckIsPriorityQueuePrioritiseOnsite() throws Exception {
        // given
        OrderRequest orderDelivery = new OrderRequest(0L, "Delivery", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));
        OrderRequest orderOnsite1 = new OrderRequest(0L, "On-site", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));
        OrderRequest orderOnsite2 = new OrderRequest(0L, "On-site", List.of(new MenuItemRequest(1L, "Chicken", 10.99, "Food"), new MenuItemRequest(2L, "Coke", 1.99, "Drinks")));


        // when
        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderOnsite1)));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderDelivery)));

        mockMvc.perform(MockMvcRequestBuilders.post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(orderOnsite2)));

        Thread.sleep(6000);

        MvcResult mvcResult = mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(3)))
                .andReturn();
        List<OrderResponse> ordersReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), new TypeReference<>() {
        });

        // then
        assertNotNull(ordersReturned.get(0).getHourAway());
        assertNull(ordersReturned.get(1).getHourAway());
        assertNotNull(ordersReturned.get(2).getHourAway());
    }
}
