package pl.jakubtworek.RestaurantManagementSystem.e2e;

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
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemRequest;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemResponse;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuItemE2ETest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnMenuItemById() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        MenuItemResponse menuItem = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemResponse.class);

        // then
        assertEquals("Chicken", menuItem.getName());
        assertEquals(10.99, menuItem.getPrice());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/id")
                        .param("id", String.valueOf(4L)))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        // then
        assertEquals(404, response.getStatus());
        assertEquals("There are no menu item in restaurant with that id: 4", response.getMessage());
    }

    @Test
    @Sql(statements = "INSERT INTO `menu` VALUES (1,'Drinks'), (2,'Food')")
    void shouldReturnCreatedMenuItem() throws Exception {
        // given
        MenuItemRequest menuItem = new MenuItemRequest(0L, "Beer", 5.99, "Drinks");

        // when
        MvcResult mvcResult = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuItemResponse menuItemReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemResponse.class);

        // then
        assertEquals("Beer", menuItemReturned.getName());
        assertEquals(5.99, menuItemReturned.getPrice());
    }

    @Test
    @Sql({"/deleting-data.sql", "/inserting-data.sql"})
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        // when
        MvcResult mvcResult = mockMvc.perform(delete("/menu-items/id")
                        .param("id", String.valueOf(1L)))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        // then
        assertEquals("Deleted menu item has id: 1", response);
    }
}
