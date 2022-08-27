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
import pl.jakubtworek.RestaurantManagementSystem.entity.Menu;
import pl.jakubtworek.RestaurantManagementSystem.entity.MenuItem;
import pl.jakubtworek.RestaurantManagementSystem.service.MenuItemService;
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
public class MenuItemRestControllerTest {
    private static MockHttpServletRequest request;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private JdbcTemplate jdbc;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    private MenuItemService menuItemService;

    @Autowired
    private MenuService menuService;

    @Autowired
    private MenuItem menuItem;

    @Autowired
    private Menu menu;

    public static final MediaType APPLICATION_JSON_UTF8 = MediaType.APPLICATION_JSON;

    @BeforeEach
    public void setupDatabase() {
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=0");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (1,'Drinks')");
        jdbc.execute("INSERT INTO Menu(id, name)" + "VALUES (2,'Food')");
        jdbc.execute("INSERT INTO Menu_Item(id, name, price, menu_id)" + "VALUES (1,'Coke', 1.29, 1)");
        jdbc.execute("INSERT INTO Menu_Item(id, name, price, menu_id)" + "VALUES (2,'Chicken', 8.99, 2)");
        //jdbc.execute("SET FOREIGN_KEY_CHECKS=1");
    }

    @Test
    public void getMenuItemsHttpRequest() throws Exception {
        menuItem.setName("Steak");
        menuItem.setPrice(12.99);
        entityManager.persist(menuItem);
        entityManager.flush();

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void getMenuItemByIdHttpRequest() throws Exception {
        MenuItem menuItem = menuItemService.findById(1);

        assertNotNull(menuItem);

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItem/id/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.name", is("Coke")))
                .andExpect(jsonPath("$.price", is("1.29")));
    }

    @Test
    public void saveMenuItemHttpRequest() throws Exception {
        menuItem.setName("Steak");

        mockMvc.perform(post("/menuItem")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(menuItem)))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(3)));
    }

    @Test
    public void deleteMenuHttpRequest() throws Exception {
        assertNotNull(menuItemService.findById(1));

        mockMvc.perform(MockMvcRequestBuilders.delete("/menuItem/{id}", 1))
                .andExpect(status().isOk());

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    public void getMenuByNameHttpRequest() throws Exception {
        List<MenuItem> menuItems = menuItemService.findByMenu("Food");

        assertNotNull(menuItems);

        mockMvc.perform(MockMvcRequestBuilders.get("/menuItems/menu/{name}", "Food"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$", hasSize(1)));
    }



    @AfterEach
    public void setupAfterTransaction() {
        jdbc.execute("DELETE FROM Menu_Item");
        jdbc.execute("DELETE FROM Menu");
    }
}
