package pl.jakubtworek.RestaurantManagementSystem.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuService;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@TestPropertySource("/application-test.properties")
@AutoConfigureMockMvc
@SpringBootTest
@Transactional
public class OrderRestControllerTest {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

/*    @Autowired
    private MenuService menuService;

    @Autowired
    private Menu menu;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (1,'Drinks')");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (2,'Food')");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void getMenusHttpRequest() throws Exception {
        menu.setName("Desserts");
        entityManager.persist(menu);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getMenuByIdHttpRequest() throws Exception {
        Menu menu = menuService.findById(1);

        assertNotNull(menu);

        mockMvc.perform(MockMvcRequestBuilders.get("/menu/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Drinks")));
    }

    @Test
    public void saveMenuHttpRequest() throws Exception {
        menu.setName("Desserts");

        mockMvc.perform(post("/menu")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menu)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void deleteMenuHttpRequest() throws Exception {
        assertNotNull(menuService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/menu/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/menus"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getMenuByNameHttpRequest() throws Exception {
        Menu menu = menuService.findByName("Drinks");

        assertNotNull(menu);

        mockMvc.perform(MockMvcRequestBuilders.get("/menu/name/{name}", "Drinks"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Drinks")));
    }



    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Menu");
    }*/

}
