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
import pl.jakubtworek.RestaurantManagementSystem.controller.menu.MenuDTO;
import pl.jakubtworek.RestaurantManagementSystem.exception.ErrorResponse;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class MenuControllerIT {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JdbcTemplate jdbc;

    @Value("${sql.script.create.menus}")
    private String sqlAddMenus;
    @Value("${sql.script.delete.menus}")
    private String sqlDeleteMenus;

    @BeforeEach
    void setup() {
        jdbc.execute(sqlAddMenus);
    }

    @AfterEach
    void delete() {
        jdbc.execute(sqlDeleteMenus);
    }

    @Test
    void shouldReturnAllMenu() throws Exception {
        mockMvc.perform(get("/menu"))
                .andExpect(status().is(200))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$", hasSize(2)));
    }

    @Test
    void shouldReturnMenuById() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu/1"))
                .andExpect(status().is(200))
                .andReturn();
        MenuDTO menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuDTO.class);

        assertThat(menuReturned).isNotNull();
        assertThat(menuReturned.getId()).isEqualTo(1L);
        assertThat(menuReturned.getName()).isEqualTo("Drinks");
    }

    @Test
    void shouldReturnErrorResponse_whenAskedForNonExistingMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(get("/menu/3"))
                .andExpect(status().isNotFound())
                .andReturn();
        ErrorResponse response = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ErrorResponse.class);

        assertThat(response).isNotNull();
        assertThat(response.getStatus()).isEqualTo(404);
        assertThat(response.getMessage()).isEqualTo("There are no menu in restaurant with that id: 3");
    }

    @Test
    void shouldReturnCreatedMenu() throws Exception {
        MenuDTO menu = new MenuDTO(3L, "Alcohol", null);

        MvcResult mvcResult = mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isCreated())
                .andReturn();
        MenuDTO menuReturned = objectMapper.readValue(mvcResult.getResponse().getContentAsString(), MenuDTO.class);

        assertThat(menuReturned).isNotNull();
        assertThat(menuReturned.getName()).isEqualTo("Alcohol");
    }

    @Test
    void shouldReturnResponseConfirmingDeletedMenu() throws Exception {
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete("/menu/2"))
                .andExpect(status().isOk())
                .andReturn();
        String response = mvcResult.getResponse().getContentAsString();

        assertThat(response).isEqualTo("Deleted menu has id: 2");
    }
}
