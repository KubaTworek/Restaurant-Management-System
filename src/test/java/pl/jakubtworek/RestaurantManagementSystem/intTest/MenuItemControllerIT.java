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
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.GetMenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuItemDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuItemControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.menus}")
    private String sqlAddMenus;
    @Value("${sql.script.create.menuitem}")
    private String sqlAddMenuItems;


    @Value("${sql.script.delete.menus}")
    private String sqlDeleteMenus;
    @Value("${sql.script.delete.menuitem}")
    private String sqlDeleteMenuItems;

    @BeforeEach
    void setup() {
        jdbc.execute(sqlAddMenus);
        jdbc.execute(sqlAddMenuItems);
    }

    @AfterEach
    void delete() {
        jdbc.execute(sqlDeleteMenus);
        jdbc.execute(sqlDeleteMenuItems);
    }

    @Test
    void shouldReturnMenuItemById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/1"))
                .andExpect(status().is(200))
                .andReturn();
        MenuItemDTO menuItem = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemDTO.class);

        assertThat(menuItem).isNotNull();
        assertThat(menuItem.getId()).isEqualTo(1L);
        assertThat(menuItem.getName()).isEqualTo("Chicken");
        assertThat(menuItem.getPrice()).isEqualTo(10.99);
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenuItem() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu-items/4"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no menu item in restaurant with that id: 4");
    }

    @Test
    void shouldReturnCreatedMenuItem() throws Exception {
        MenuDTO menu = new MenuDTO(3L, "Alcohol", null);
        GetMenuItemDTO menuItem = new GetMenuItemDTO(4L, "Beer", 5.99, menu);

        MvcResult mvcResult = mockMvc.perform(post("/menu-items")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuItemDTO menuItemGet = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuItemDTO.class);

        assertThat(menuItemGet).isNotNull();
        assertThat(menuItemGet.getName()).isEqualTo("Beer");
        assertThat(menuItemGet.getPrice()).isEqualTo(5.99);}

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/menu-items/1"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted menu item has id: 1");
    }
}
